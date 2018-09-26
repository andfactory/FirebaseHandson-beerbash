package com.hogehoge.newsapp.repo

import com.hogehoge.newsapp.api.NewsSourceApi
import com.hogehoge.newsapp.model.NewsArticles
import com.hogehoge.newsapp.model.NewsSource
import io.reactivex.Single

class NewsRepository {

    private val favoriteArticles = ArrayList<NewsArticles>()
    private val api = NewsSourceApi.getNewsSourceService()

    fun fetchAllData(): Single<NewsSource> {
        return api.getNewsSource()
    }

    fun query(word: String): Single<NewsSource> {
        return api.query(word)
    }

    fun favorite(articles: NewsArticles): Int {
        favoriteArticles.add(articles)
        return favoriteArticles.size
    }

    fun unfavorite(articles: NewsArticles): Int {
        favoriteArticles.remove(articles)
        return favoriteArticles.size
    }
}