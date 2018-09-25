package com.hogehoge.newsapp.repo

import com.hogehoge.newsapp.api.NewsSourceApi
import com.hogehoge.newsapp.model.NewsArticles
import com.hogehoge.newsapp.model.NewsSource
import io.reactivex.Single

class NewsRepository {

    private val favoriteArticles = ArrayList<NewsArticles>()

    fun fetchAllData(): Single<NewsSource> {
        return NewsSourceApi.getNewsSourceService().getNewsSource()
    }

    fun query(word: String): Single<NewsSource> {
        return NewsSourceApi.getNewsSourceService().query(word)
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