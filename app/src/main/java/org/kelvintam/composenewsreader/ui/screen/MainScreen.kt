package org.kelvintam.composenewsreader.ui.screen

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.*
import org.kelvintam.composenewsreader.CNRViewModel
import org.kelvintam.composenewsreader.datamodel.NewsModel
import org.kelvintam.composenewsreader.datamodel.RetrofitHelper
import org.kelvintam.composenewsreader.ui.screen.destinations.NewsDetailDestination

const val TAG = "MainScreen"

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(viewModel: CNRViewModel, navigator: DestinationsNavigator) {
    //val newsList = remember { viewModel.newsList }
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

@Destination
@Composable
fun NewsDetail(
    navigator: DestinationsNavigator,
    article: NewsModel.Article
) {
    val uriHandler = LocalUriHandler.current
    Column(Modifier.fillMaxSize()) {
        Button(
            onClick = { navigator.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Return")
        }
        Text(article.title)
        article.description?.let { Text(it) }
        Text(article.publishedAt)
        Text("${article.author}, ${article.source.name}")
        SubcomposeAsyncImage(
            model = article.urlToImage,
            loading = {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            },
            contentDescription = article.description
        )
        Text(article.content)
        Button(
            onClick = { uriHandler.openUri(article.url) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Read full article in browser")
        }
    }
}