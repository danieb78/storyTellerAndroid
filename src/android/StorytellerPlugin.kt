package com.example.benficastoryteller

import android.content.Context
import android.content.Intent
import android.util.Log
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CallbackContext
import org.apache.cordova.PluginResult
import org.json.JSONArray

class CDVStoryteller : CordovaPlugin() {
    private var controller: StorytellerController? = null

    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        return when (action) {
            "initializeSDK" -> {
                val apiKey = args.optString(0)
                val userId = args.optString(1)
                initializeSDK(apiKey, userId, callbackContext)
                true
            }
            "showStorytellerView" -> {
                showStorytellerView(callbackContext)
                true
            }
            else -> false
        }
    }

    private fun initializeSDK(apiKey: String?, userId: String?, callbackContext: CallbackContext) {
        if (apiKey.isNullOrEmpty()) {
            callbackContext.sendPluginResult(PluginResult(PluginResult.Status.ERROR, "API key is missing."))
            return
        }

        if (userId.isNullOrEmpty()) {
            callbackContext.sendPluginResult(PluginResult(PluginResult.Status.ERROR, "User ID is missing."))
            return
        }

        controller = StorytellerController(cordova.context, apiKey, userId)
        controller?.initializeStoryteller(
            onSuccess = {
                Log.d("CDVStoryteller", "Storyteller SDK initialized for user: $userId")
                val result = PluginResult(PluginResult.Status.OK, "SDK initialized for user: $userId")
                callbackContext.sendPluginResult(result)
            },
            onFailure = { errorMessage ->
                Log.e("CDVStoryteller", "SDK Init Error: $errorMessage")
                val result = PluginResult(PluginResult.Status.ERROR, errorMessage.message)
                callbackContext.sendPluginResult(result)
            }
        )
    }

    private fun showStorytellerView(callbackContext: CallbackContext) {
        val intent = Intent(cordova.context, StorytellerViewActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        cordova.context.startActivity(intent)
        val result = PluginResult(PluginResult.Status.OK, "Storyteller view presented.")
        callbackContext.sendPluginResult(result)
        Log.d("CDVStoryteller", "Storyteller view presented.")
    }
}
