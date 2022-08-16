package org.kelvintam.composenewsreader.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kelvintam.composenewsreader.datamodel.CNRViewModel
import org.kelvintam.composenewsreader.ui.component.NewsCard
import org.kelvintam.composenewsreader.R
import org.kelvintam.composenewsreader.datamodel.RetrofitHelper

@Destination
@Composable
fun NewsList(navigator: DestinationsNavigator, viewModel: CNRViewModel) {
    val TAG = "News List"
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.d(TAG, "Main Screen")
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = { Text(text = "Search here") },
            value = viewModel.searchBoxText,
            onValueChange = { viewModel.searchBoxText = it },
            singleLine = true,
            shape = RoundedCornerShape(5.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_search_24), "")
            },
            trailingIcon = {
                if (viewModel.searchBoxText.isNotBlank()) {
                    TextButton(onClick = {
                        val encodedString =
                            java.net.URLEncoder.encode(viewModel.searchBoxText, "utf-8")
                        CoroutineScope(Dispatchers.IO).launch {
                            val result =
                                RetrofitHelper.getNewsFromTopicCall(encodedString)
                            if (result != null) {
                                viewModel.newsList = result
                            }
                        }
                    }) {
                        Text("Search")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    val encodedString =
                        java.net.URLEncoder.encode(viewModel.searchBoxText, "utf-8")
                    CoroutineScope(Dispatchers.IO).launch {
                        val result =
                            RetrofitHelper.getNewsFromTopicCall(encodedString)
                        if (result != null) {
                            viewModel.newsList = result
                        }
                    }
                }
            ),
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = viewModel.newsList, key = { it.url }) { article ->
                NewsCard(navigator, article) { viewModel.writeRecentReadListToPref(it) }
            }
        }
    }
}