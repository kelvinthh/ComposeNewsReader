package org.kelvintam.composenewsreader.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.kelvintam.composenewsreader.datamodel.CNRViewModel
import org.kelvintam.composenewsreader.ui.component.NewsCard

@Destination
@Composable
fun HeadlineNews(navigator: DestinationsNavigator, viewModel: CNRViewModel) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        if (viewModel.headlineList.isEmpty()) {
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
        items(items = viewModel.headlineList, key = { it.url }) { article ->
            NewsCard(navigator, article) { viewModel.writeRecentReadListToPref(it) }
        }
    }
}
