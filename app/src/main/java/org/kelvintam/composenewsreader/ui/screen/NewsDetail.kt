package org.kelvintam.composenewsreader.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.kelvintam.composenewsreader.datamodel.NewsModel

@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun NewsDetail(
    navigator: DestinationsNavigator,
    article: NewsModel.Article
) {
    val uriHandler = LocalUriHandler.current
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navigator.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Return")
        }
        Card(elevation = 10.dp) {
            Column(Modifier.padding(10.dp)) {

                Text(article.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)

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
                    contentDescription = article.description,
                    modifier = Modifier.fillMaxWidth()
                )

                article.content?.let { Text(it) }
            }
        }
        Button(
            onClick = { uriHandler.openUri(article.url) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Read full article in browser")
        }
    }
}