package org.kelvintam.composenewsreader.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.kelvintam.composenewsreader.datamodel.CNRViewModel
import org.kelvintam.composenewsreader.ui.component.NewsCard
import org.kelvintam.composenewsreader.ui.theme.TextStyle

@Destination
@Composable
fun HeadlineNews(
    navigator: DestinationsNavigator,
    viewModel: CNRViewModel,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(viewModel.isHeadLinesRefreshing),
        onRefresh = { viewModel.refreshHeadlines() },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = state,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                backgroundColor = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.small,
            )
        }) {

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
            item{
                Text("Welcome!", style = TextStyle.welcome, modifier = Modifier.padding(horizontal = 15.dp))
            }
            items(items = viewModel.headlineList, key = { it.url }) { article ->
                NewsCard(navigator, article) { viewModel.writeRecentReadListToPref(it) }
            }
        }
    }
}
