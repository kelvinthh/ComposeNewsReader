package org.kelvintam.composenewsreader.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun NewsDetail(
    navigator: DestinationsNavigator,
    article: NewsModel.Article
) {
    val uriHandler = LocalUriHandler.current
    val parsedDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(
            article.publishedAt
        ) as Date
    )

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
        Card(elevation = 10.dp, modifier = Modifier.heightIn(300.dp, 800.dp)) {
            LazyColumn(Modifier.padding(10.dp)) {
                item{
                    Text(article.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)

                    article.description?.let { Text(it) }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(parsedDateTime)

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

                    Spacer(modifier = Modifier.height(10.dp))

                    article.content?.let { Text(it) }
                }
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