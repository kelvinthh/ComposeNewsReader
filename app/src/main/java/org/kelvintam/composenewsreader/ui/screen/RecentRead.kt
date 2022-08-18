package org.kelvintam.composenewsreader.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.kelvintam.composenewsreader.datamodel.CNRViewModel
import org.kelvintam.composenewsreader.ui.component.NewsCard

@Composable
fun RecentRead(navigator: DestinationsNavigator, viewModel: CNRViewModel) {
    if (viewModel.recentReadList.isEmpty()) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("You haven't read any news yet :(")
        }
    }
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(items = viewModel.recentReadList, key = { it.url }) { article ->
            NewsCard(navigator, article, viewModel.currentTab, viewModel.recentReadList) { viewModel.writeRecentReadListToPref(it) }
        }
    }
}