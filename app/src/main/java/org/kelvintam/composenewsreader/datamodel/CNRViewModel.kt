package org.kelvintam.composenewsreader.datamodel

import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CNRViewModel(
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    init {
        refreshHeadlines()
    }

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    var currentTab by mutableStateOf(Tabs.HEADLINES)

    var searchBoxText by mutableStateOf("")

    val recentReadList = mutableStateListOf<NewsModel.Article>()
    var newsList by mutableStateOf(emptyList<NewsModel.Article>())
    var headlineList by mutableStateOf(emptyList<NewsModel.Article>())
    val isHeadLinesRefreshing by mutableStateOf(false)

    //////////////// News search setting ////////////////
    var sortBy by mutableStateOf(SortBy.PUBLISHED_AT)
    /////////////////////////////////////x////////////////

    fun writeRecentReadListToPref(article: NewsModel.Article? = null) {
        if(article != null){
            if (recentReadList.contains(article)) return
            recentReadList.add(0, article)
        }
        val json = Gson().toJson(recentReadList)
        editor.putString("recentReadList", json)
        editor.commit()
    }

    private fun readRecentReadListFromPref() {
        val articleList: List<NewsModel.Article>
        val serializedObject = sharedPreferences.getString("recentReadList", null)
        if (serializedObject != null) {
            val type = object : TypeToken<List<NewsModel.Article>>() {}.type
            articleList = Gson().fromJson(serializedObject, type)
            recentReadList.addAll(articleList)
        }
    }

    fun refreshHeadlines(){
        viewModelScope.launch(Dispatchers.IO) {
            readRecentReadListFromPref()
            val headlines = RetrofitHelper.getHeadLinesCall("us")
            if (headlines != null) {
                headlineList = headlines
            }
        }
    }
}