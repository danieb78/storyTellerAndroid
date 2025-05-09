package com.example.benficastoryteller

import android.util.Log
import com.storyteller.domain.entities.Error
import com.storyteller.ui.list.StorytellerListViewDelegate

class StorytellerHandler : StorytellerListViewDelegate {
    override fun onPlayerDismissed() {
        Log.d("StorytellerHandler", "📴 Storyteller Player Dismissed")
    }

    override fun onDataLoadStarted() {
        Log.d("StorytellerHandler", "⏳ Storyteller data loading started...")
    }

    override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
        if (success) {
            Log.d("StorytellerHandler", "✅ Data loaded successfully! $dataCount stories available.")
            if (dataCount == 0) {
                Log.w("StorytellerHandler", "⚠️ No stories available. Check API content.")
            }
        } else if (error != null) {
            Log.e("StorytellerHandler", "❌ Error loading stories: ${error.message}")
        }
    }
}
