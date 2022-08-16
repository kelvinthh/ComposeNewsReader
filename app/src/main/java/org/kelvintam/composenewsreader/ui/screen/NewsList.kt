package org.kelvintam.composenewsreader.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kelvintam.composenewsreader.CNRViewModel
import org.kelvintam.composenewsreader.datamodel.RetrofitHelper
import org.kelvintam.composenewsreader.ui.screen.destinations.NewsDetailDestination

@Destination
@Composable
fun NewsList(navigator: DestinationsNavigator, viewModel: CNRViewModel) {
    val TAG = "News List"
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.d(TAG, "Main Screen")

        Text("Compose News Reader")

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val result =
                        RetrofitHelper.getNewsFromTopicCall("Macbook", "2022-08-15", "popularity")
                    if (result != null) {
                        viewModel.newsList = result
                    }
                }
            }) {
                Text("Get News")
            }

            Button(onClick = {
                if (viewModel.newsList.isNotEmpty()) {
                    viewModel.newsList.forEach {
                        Log.d(TAG, it.title)
                    }
                }
            }) {
                Text("Log")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = viewModel.newsList, key = { it.url }) {
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
}