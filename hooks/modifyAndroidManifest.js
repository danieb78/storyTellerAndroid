#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const xml2js = require('xml2js');

// Adjust the path as needed
const manifestPath = path.join(__dirname, '../../../platforms/android/app/src/main/AndroidManifest.xml');

console.log(`Modifying AndroidManifest.xml at path: ${manifestPath}`);

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

        // Ensure application array exists
        if (!result.manifest.application) {
            result.manifest.application = [{}];
        }

        const app = result.manifest.application[0];
        
        // Add Tools Namespace in AndroidManifest.xml
        if (!result.manifest.$) {
            result.manifest.$ = {};
        }
        
        if (result.manifest.$['xmlns:tools'] === undefined) {
            result.manifest.$['xmlns:tools'] = 'http://schemas.android.com/tools';
        }

        // Ensure activity array exists
        if (!app.activity) {
            app.activity = [];
        }
        
        const activityIndex = app.activity.findIndex(activity => activity.$['android:name'] === 'com.example.benficastoryteller.StorytellerViewActivity');
        
        if (activityIndex === -1) {
            app.activity.push({
                $: {
                    'android:exported': 'true',
                    'android:name': 'com.example.benficastoryteller.StorytellerViewActivity'
                }
            });
            console.log("Activity added.");
        } else {
            console.log("Activity already exists.");
        }

        // Ensure provider array exists
        if (!app.provider) {
            app.provider = [];
        } 
        
        const providers = app.provider;
        const providerIndex = providers.findIndex(provider => provider.$['android:name'] === 'androidx.core.content.FileProvider');

        const customProvider = {
            $: {
                'android:authorities': 'com.example.benficastoryteller.cdv.core.file.provider',
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

        if (providerIndex !== -1 && !providers[providerIndex]['meta-data']) {
            providers[providerIndex]['meta-data'] = [];
        }

        // Custom meta-data Declaration in AndroidManifest.xml
        const metaData = {
            $: {
                'android:name': 'android.support.FILE_PROVIDER_PATHS',
                'android:resource': '@xml/cdv_core_file_provider_paths',
                'tools:replace': 'android:resource'
            }
        };

        if (providerIndex !== -1) {
            const metaDataIndex = providers[providerIndex]['meta-data'].findIndex(meta => meta.$['android:name'] === 'android.support.FILE_PROVIDER_PATHS');
            
            if (metaDataIndex !== -1) {
                providers[providerIndex]['meta-data'][metaDataIndex] = metaData;
            } else {
                providers[providerIndex]['meta-data'].push(metaData);
            }
        }

        app.provider = providers;

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
