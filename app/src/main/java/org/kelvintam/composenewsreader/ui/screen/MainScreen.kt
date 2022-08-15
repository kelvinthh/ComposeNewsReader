package org.kelvintam.composenewsreader.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.*
import org.kelvintam.composenewsreader.CNRViewModel
import org.kelvintam.composenewsreader.datamodel.RetrofitHelper

const val TAG = "MainScreen"
@Composable
fun MainScreen(viewModel: CNRViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.d(TAG, "Main Screen")
        Text("Basic Text")
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val result = RetrofitHelper.getNewsFromTopicCall("Macbook", "2022-08-15", "popularity")
                result?.forEach{
                    Log.d(TAG, it.source.name)
                    Log.d(TAG, it.title)
                    Log.d(TAG, it.description)
                }
            }
        }) {
            Text("Button")
        }
    }
}