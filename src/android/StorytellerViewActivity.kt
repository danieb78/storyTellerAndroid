package com.example.benficastoryteller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.storyteller.ui.compose.components.lists.grid.StorytellerClipsGrid
import com.storyteller.ui.compose.components.lists.grid.StorytellerStoriesGrid
import com.storyteller.ui.compose.components.lists.grid.rememberStorytellerGridState
import com.storyteller.ui.compose.components.lists.row.StorytellerClipsRow
import com.storyteller.ui.compose.components.lists.row.StorytellerStoriesRow
import com.storyteller.ui.compose.components.lists.row.rememberStorytellerRowState

class StorytellerViewActivity : ComponentActivity() {
    private lateinit var storytellerController: StorytellerController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        storytellerController = StorytellerController(this, "","")
        setContent {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(storytellerController)
                }
        }
    }
}

@Composable
fun MainScreen(controller: StorytellerController) {
    val listViewDelegate = remember { controller.getListViewDelegate() }
    val storiesDataModel = remember { controller.getStoriesDataModel() }
    val clipsDataModel = remember { controller.getClipsDataModel() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Stories",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        // Stories Row
        StorytellerStoriesRow(
            modifier = Modifier
                .fillMaxSize()
                .height(120.dp),
            dataModel = storiesDataModel,
            delegate = listViewDelegate,
            state = rememberStorytellerRowState()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stories Grid
        StorytellerStoriesGrid(
            modifier = Modifier
                .fillMaxSize()
                .height(300.dp),
            dataModel = storiesDataModel,
            delegate = listViewDelegate,
            state = rememberStorytellerGridState(),
            isScrollable = true,
            isEnabled = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Clips",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        // Clips Row
        StorytellerClipsRow(
            modifier = Modifier
                .fillMaxSize()
                .height(120.dp),
            dataModel = clipsDataModel,
            delegate = listViewDelegate,
            state = rememberStorytellerRowState()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Clips Scrollable Grid
        StorytellerClipsGrid(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            dataModel = clipsDataModel,
            delegate = listViewDelegate,
            state = rememberStorytellerGridState(),
            isScrollable = true,
            isEnabled = true
        )
    }
}
