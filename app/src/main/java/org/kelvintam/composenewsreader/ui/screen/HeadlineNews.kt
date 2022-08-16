package org.kelvintam.composenewsreader.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.kelvintam.composenewsreader.CNRViewModel
import org.kelvintam.composenewsreader.ui.screen.destinations.NewsDetailDestination

@Destination
@Composable
fun HeadlineNews(navigator: DestinationsNavigator, viewModel: CNRViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(items = viewModel.headlineList, key = { it.url }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigator.navigate(NewsDetailDestination(it))
                    }) {
                Text(it.title)
                it.description?.let { description ->
                    Text(
                        description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text("${it.source.name} ${it.publishedAt}")
                Spacer(modifier = Modifier.height(5.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray,
                    thickness = 2.dp
                )
            }
        }
    }
}
