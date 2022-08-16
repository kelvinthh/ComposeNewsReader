package org.kelvintam.composenewsreader.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import coil.compose.SubcomposeAsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.kelvintam.composenewsreader.datamodel.NewsModel

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
        article.content?.let { Text(it) }
        Button(
            onClick = { uriHandler.openUri(article.url) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Read full article in browser")
        }
    }
}