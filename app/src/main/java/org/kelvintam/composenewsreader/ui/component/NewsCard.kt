@file:OptIn(ExperimentalFoundationApi::class)

package org.kelvintam.composenewsreader.ui.component

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.kelvintam.composenewsreader.datamodel.CNRViewModel
import org.kelvintam.composenewsreader.datamodel.NewsModel
import org.kelvintam.composenewsreader.datamodel.Tabs
import org.kelvintam.composenewsreader.ui.screen.destinations.NewsDetailDestination
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun NewsCard(
    navigator: DestinationsNavigator,
    article: NewsModel.Article,
    currentTab: Tabs? = null,
    recentReadList: MutableList<NewsModel.Article>? = null,
    writeRecentReadListToPref: (NewsModel.Article?) -> Unit,
) {
    val openDialog = remember { mutableStateOf(false) }
    val parsedDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm").format(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(
            article.publishedAt
        ) as Date
    )

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .combinedClickable(
                onClick = {
                    writeRecentReadListToPref(article)
                    navigator.navigate(NewsDetailDestination(article))
                },
                onLongClick = {
                    if (currentTab == Tabs.RECENT_READ) {
                        openDialog.value = true
                    }
                },
            )

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

                Text("${article.source.name}")
                Text(parsedDateTime)

                Spacer(modifier = Modifier.height(5.dp))

            }
        }

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text(text = "Remove ${article.title} from your recent read list?") },
                text = { Text("The article will be removed from the list.") },
                confirmButton = {
                    Button(
                        onClick = {
                            recentReadList?.remove(article)
                            writeRecentReadListToPref(null)
                            openDialog.value = false
                        }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}