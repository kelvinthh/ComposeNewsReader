package org.kelvintam.composenewsreader

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kelvintam.composenewsreader.datamodel.NewsModel
import org.kelvintam.composenewsreader.datamodel.RetrofitHelper
import org.kelvintam.composenewsreader.ui.screen.MainScreen
import org.kelvintam.composenewsreader.ui.screen.NavGraphs
import org.kelvintam.composenewsreader.ui.screen.destinations.MainScreenDestination
import org.kelvintam.composenewsreader.ui.theme.ComposeNewsReaderTheme


class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = this.getSharedPreferences("preference_key", Context.MODE_PRIVATE)
        val viewModel = CNRViewModel(sharedPreferences)
        setContent {
            ComposeNewsReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root) {
                        composable(MainScreenDestination) {
                            Column(Modifier.fillMaxSize()) {
                                MainScreen(viewModel, destinationsNavigator)
                            }
                        }
                    }
                }
            }
        }
    }
}

class CNRViewModel(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            readRecentReadListFromPref()
            val headlines = RetrofitHelper.getHeadLinesCall("us")
            if (headlines != null) {
                headlineList = headlines
            }
        }
    }

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    val recentReadList = mutableListOf<NewsModel.Article>()

    var newsList by mutableStateOf(emptyList<NewsModel.Article>())
    var headlineList by mutableStateOf(emptyList<NewsModel.Article>())

    fun writeRecentReadListToPref(article: NewsModel.Article) {
        if (recentReadList.contains(article)) return
        recentReadList.add(article)
        val json = Gson().toJson(recentReadList)
        editor.putString("recentReadList", json)
        editor.commit()
    }

    private fun readRecentReadListFromPref() {
        val articleList: List<NewsModel.Article>
        val serializedObject = sharedPreferences.getString("recentReadList", null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<List<NewsModel.Article>>() {}.type
            articleList = gson.fromJson(serializedObject, type)
            recentReadList.addAll(articleList)
        }
    }
}