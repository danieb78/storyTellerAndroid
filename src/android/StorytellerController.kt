package com.example.benficastoryteller

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.storyteller.Storyteller
import com.storyteller.data.StorytellerClipsDataModel
import com.storyteller.data.StorytellerStoriesDataModel
import com.storyteller.domain.entities.Error
import com.storyteller.domain.entities.UserInput
import com.storyteller.ui.list.StorytellerListViewDelegate

class StorytellerController(private val context: Context, private val apiKey: String, private val userId: String) {

    fun initializeStoryteller(onSuccess: () -> Unit, onFailure: (Error) -> Unit) {
        if (isNetworkAvailable()) {
            Storyteller.initialize(
                apiKey = apiKey,
                userInput = UserInput(userId),
                onSuccess = {
                    Log.d("StorytellerController", "Success Initializing App")
                    onSuccess()
                },
                onFailure = { error ->
                    Log.d("StorytellerController", "Error: $error")
                    onFailure(error)
                }
            )
        } else {
            Log.d("StorytellerController", "No internet connection available")
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun getListViewDelegate(): StorytellerListViewDelegate {
        return StorytellerHandler()
    }

    fun getStoriesDataModel(): StorytellerStoriesDataModel {
        return StorytellerStoriesDataModel(categories = listOf("benfica-top-row", "benfica-singleton", "benfica-moments"))
    }

    fun getClipsDataModel(): StorytellerClipsDataModel {
        return StorytellerClipsDataModel(collection = "benfica-moments")
    }
}
