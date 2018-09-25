package com.hogehoge.newsapp.model

import com.google.gson.annotations.SerializedName

/**
 * News Article Model describing the article details
 * fetched from news source.
 *
 * @author hogehoge Chordiya
 * @since 5/23/2017.
 */
data class NewsArticles(
        @SerializedName("author") var author: String? = null,
        @SerializedName("title") var title: String = "",
        @SerializedName("description") var description: String? = "",
        @SerializedName("url") var url: String = "",
        @SerializedName("favorite") var favorite: Boolean = false,
        @SerializedName("urlToImage") var urlToImage: String? = null,
        @SerializedName("publishedAt") var publishedAt: String? = null)