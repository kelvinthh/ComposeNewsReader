package org.kelvintam.composenewsreader.datamodel

import okhttp3.ResponseBody
import org.kelvintam.composenewsreader.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming

const val API_URL = "https://newsapi.org/v2/everything?"
const val API_KEYS = BuildConfig.NEWS_API_KEY

interface ApiService {

}

class RetrofitHelper {
}