package com.andfactory.newsapp.utils

import com.andfactory.newsapp.model.NewsArticles

/**
 * @author andfactory Chordiya
 * @version 1.0
 * @since 08/11/2017.
 */

object TestDataUtils {

    fun createNewsArticles(vararg newsArticles: NewsArticles): List<NewsArticles> {
        return newsArticles.toList()
    }
}
