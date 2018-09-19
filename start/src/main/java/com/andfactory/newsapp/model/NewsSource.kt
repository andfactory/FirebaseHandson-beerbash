package com.andfactory.newsapp.model

import com.google.gson.annotations.SerializedName

/**
 * News source model describing the source details
 * and the articles under it.
 *
 * @author andfactory Chordiya
 * @since 5/23/2017.
 */
data class NewsSource(
        @SerializedName("status") var status: String = "",
        @SerializedName("source") var source: String = "",
        @SerializedName("sortBy") var sortBy: String = "",
        @SerializedName("articles") var articles: List<NewsArticles> = emptyList())