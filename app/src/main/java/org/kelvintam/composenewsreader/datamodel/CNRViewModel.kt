package org.kelvintam.composenewsreader.datamodel

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    var currentTab by mutableStateOf(Tabs.HEADLINES)

    var searchBoxText by mutableStateOf("")

    val recentReadList = mutableListOf<NewsModel.Article>()
    var newsList by mutableStateOf(emptyList<NewsModel.Article>())
    var headlineList by mutableStateOf(emptyList<NewsModel.Article>())

    fun writeRecentReadListToPref(article: NewsModel.Article) {
        if (recentReadList.contains(article)) return
        recentReadList.add(0, article)
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
}