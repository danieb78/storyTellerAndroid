#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const xml2js = require('xml2js');

const manifestPath = path.join(__dirname, '../platforms/android/app/src/main/AndroidManifest.xml');

fs.readFile(manifestPath, 'utf8', (err, data) => {
    if (err) {
        console.error("Failed to read AndroidManifest.xml:", err);
        return;
    }

    xml2js.parseString(data, (err, result) => {
        if (err) {
            console.error("Failed to parse AndroidManifest.xml:", err);
            return;
        }

        // Add Tools Namespace in AndroidManifest.xml
        if (result.manifest.$['xmlns:tools'] === undefined) {
            result.manifest.$['xmlns:tools'] = 'http://schemas.android.com/tools';
        }

        // Custom Activity Declaration in AndroidManifest.xml
        if (result.manifest.application && result.manifest.application[0].activity === undefined) {
            result.manifest.application[0].activity = [{
                $: {
                    'android:exported': 'true',
                    'android:name': 'com.example.benficastoryteller.StorytellerViewActivity'
                }
            }];
        }

        // Ensure internet permission
        if (!result.manifest['uses-permission']) {
            result.manifest['uses-permission'] = [];
        }
        result.manifest['uses-permission'].push({
            $: {
                'android:name': 'android.permission.INTERNET'
            }
        });

        // Custom Provider Declaration in AndroidManifest.xml
        const providers = result.manifest.application[0].provider || [];
        const providerIndex = providers.findIndex(provider => provider.$['android:name'] === 'androidx.core.content.FileProvider');
        
        const customProvider = {
            $: {
                'android:authorities': '${applicationId}.cdv.core.file.provider',
                'android:exported': 'false',
                'android:grantUriPermissions': 'true',
                'android:name': 'androidx.core.content.FileProvider',
                'tools:replace': 'android:authorities'
            }
        };
        
        if (providerIndex !== -1) {
            providers[providerIndex] = customProvider;
        } else {
            providers.push(customProvider);
        }
        result.manifest.application[0].provider = providers;

        // Custom meta-data Declaration in AndroidManifest.xml
        const metaData = {
            $: {
                'android:name': 'android.support.FILE_PROVIDER_PATHS',
                'android:resource': '@xml/cdv_core_file_provider_paths',
                'tools:replace': 'android:resource'
            }
        };

        const metaDataIndex = providers[providerIndex]['meta-data'].findIndex(meta => meta.$['android:name'] === 'android.support.FILE_PROVIDER_PATHS');
        
        if (metaDataIndex !== -1) {
            providers[providerIndex]['meta-data'][metaDataIndex] = metaData;
        } else {
            providers[providerIndex]['meta-data'] = [metaData];
        }
        
        // Convert back to XML
        const builder = new xml2js.Builder();
        const modifiedXml = builder.buildObject(result);

        fs.writeFile(manifestPath, modifiedXml, 'utf8', (err) => {
            if (err) {
                console.error("Failed to write modified AndroidManifest.xml:", err);
                return;
            }
            console.log("AndroidManifest.xml has been modified successfully.");
        });
    });
});
