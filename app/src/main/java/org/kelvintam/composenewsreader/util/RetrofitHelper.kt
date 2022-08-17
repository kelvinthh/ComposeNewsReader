package org.kelvintam.composenewsreader.datamodel

import android.util.Log
import org.intellij.lang.annotations.Language
import org.kelvintam.composenewsreader.BuildConfig
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://newsapi.org/v2/"
const val API_KEYS = BuildConfig.NEWS_API_KEY
const val LANG = "en"

const val TAG = "Retrofit Helper"

interface ApiService {
    @GET("everything")
    suspend fun getNewsFromTopic(
        @Query("apiKey") apiKey: String = API_KEYS,
        @Query("q") q: String? = null,
        @Query("searchIn") searchIn: String? = null,
        @Query("sources") sources: String? = null,
        @Query("domains") domains: String? = null,
        @Query("excludeDomains") excludeDomains: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("language") language: String = LANG,
        @Query("sortBy") sortBy: String? = "publishedAt",
        @Query("pageSize") pageSize: Int? = 100,
        @Query("page") page: Int? = 1,
    ): Response<NewsModel>

    @GET("top-headlines")
    suspend fun getHeadlines(
        @Query("apiKey") apiKey: String = API_KEYS,
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("sources") sources: String? = null,
        @Query("q") q: String? = null,
    ): Response<NewsModel>
}

object RetrofitHelper {
    private val api by lazy {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun getResult(newsFromTopic: Response<NewsModel>): List<NewsModel.Article>? {
        kotlin.runCatching {
            newsFromTopic
        }.onFailure {
            Log.d(TAG, it.toString())
            return null
        }.onSuccess {
            if (it.isSuccessful && it.body()?.articles?.isNotEmpty() == true)
                return it.body()?.articles
            return null
        }
        return null
    }

    // ================= Public functions // =================
    suspend fun getNewsFromTopicCall(
        q: String?,
        sortBy: SortBy = SortBy.PUBLISHED_AT
    ): List<NewsModel.Article>? {
        return getResult(api.getNewsFromTopic(q = q, sortBy = sortBy.value))
    }

    suspend fun getHeadLinesCall(country: String?): List<NewsModel.Article>? {
        return getResult(api.getHeadlines(country = country))
    }
}

enum class SortBy(val value: String, val title: String) {
    PUBLISHED_AT("publishedAt", "Published At"),
    POPULARITY("popularity", "Popularity"),
    RELEVANCY("relevancy", "Relevancy")
}