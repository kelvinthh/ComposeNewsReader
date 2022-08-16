package org.kelvintam.composenewsreader.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.kelvintam.composenewsreader.datamodel.NewsModel
import org.kelvintam.composenewsreader.ui.screen.destinations.NewsDetailDestination

@Composable
fun NewsCard(navigator: DestinationsNavigator, article: NewsModel.Article) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .clickable {
                navigator.navigate(NewsDetailDestination(article))
            }
    ) {

        Card(elevation = 10.dp) {
            Column(Modifier.padding(10.dp)) {
                Text(article.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)

                article.description?.let { description ->
                    Text(
                        description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text("${article.source.name} ${article.publishedAt}")

                Spacer(modifier = Modifier.height(5.dp))

            }
        }
    }
}