package org.kelvintam.composenewsreader.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
) : Parcelable {
    @Parcelize
    data class Article(
        val author: String?,
        val content: String?,
        val description: String?,
        val publishedAt: String,
        val source: Source,
        val title: String,
        val url: String,
        val urlToImage: String?
    ) : Parcelable {
        @Parcelize
        data class Source(
            val id: String?,
            val name: String?
        ) : Parcelable
    }
}