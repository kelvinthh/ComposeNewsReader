package org.kelvintam.composenewsreader.ui.screen

import android.util.Log
import android.widget.Space
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
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlinx.coroutines.*
import org.kelvintam.composenewsreader.CNRViewModel
import org.kelvintam.composenewsreader.datamodel.RetrofitHelper

const val TAG = "MainScreen"

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(viewModel: CNRViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.d(TAG, "Main Screen")

        Text("Compose News Reader")

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
        val articles = viewModel.newsList
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = articles, key = { it.url }) {
                Column(Modifier.fillMaxWidth().clickable {

                }) {
                    Text(it.title)
                    Text(it.description)
                    Text(it.publishedAt)
                    Text("${it.author}, ${it.source.name}")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(it.content)
                    Spacer(modifier = Modifier.height(5.dp))
                    Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray)
                }
            }
        }
    }
}