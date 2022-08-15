package org.kelvintam.composenewsreader.datamodel

import android.util.Log
import org.kelvintam.composenewsreader.BuildConfig
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://newsapi.org/v2/"
const val API_KEYS = BuildConfig.NEWS_API_KEY

const val TAG = "Retrofit Helper"

interface ApiService {
    @GET("everything")
    suspend fun getNewsFromTopic(
        @Query("q") q: String?,
        @Query("from") from: String?,
        @Query("sortBy") sortBy: String?,
        @Query("apiKey") apiKey: String = API_KEYS
    ): Response<NewsModel>

    @GET("top-headlines")
    suspend fun getHeadlines(
        @Query("country") country: String?,
        @Query("source") source: String?,
        @Query("apiKey") apiKey: String = API_KEYS
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
        from: String?,
        sortBy: String?
    ): List<NewsModel.Article>? {
        return getResult(api.getNewsFromTopic(q, from, sortBy))
    }

    suspend fun getHeadLinesCall(country: String?, source: String?): List<NewsModel.Article>? {
        return getResult(api.getHeadlines(country, source))
    }
}