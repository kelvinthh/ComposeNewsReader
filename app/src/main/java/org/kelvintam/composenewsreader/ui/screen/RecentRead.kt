package org.kelvintam.composenewsreader.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.kelvintam.composenewsreader.CNRViewModel
import org.kelvintam.composenewsreader.ui.component.NewsCard

@Composable
fun RecentRead(navigator: DestinationsNavigator, viewModel: CNRViewModel) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        if (viewModel.recentReadList.isEmpty()) {
            item {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(Modifier.padding(top = 20.dp))
                }
            }
            return@LazyColumn
        }
        items(items = viewModel.recentReadList, key = { it.url }) { article ->
            NewsCard(navigator, article) { viewModel.writeRecentReadListToPref(it) }
        }
    }
}