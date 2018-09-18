package com.akshay.newsapp.ui.top

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.akshay.newsapp.api.NewsSourceService
import com.akshay.newsapp.db.DatabaseCreator
import com.akshay.newsapp.model.NewsArticles
import com.akshay.newsapp.model.network.Resource
import com.akshay.newsapp.repo.NewsRepository

/**
 * A container for [NewsArticles] related data to show on the UI.
 */
class SummaryViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var newsArticles: LiveData<Resource<List<NewsArticles>?>>
    private val newsRepository: NewsRepository = NewsRepository(
            DatabaseCreator.database(application).newsArticlesDao(),
            NewsSourceService.getNewsSourceService()
    )

    fun query(word: String) {
        newsArticles = newsRepository.query(word)
    }

    /**
     * Return news articles to observe on the UI.
     */
    fun getNewsArticles() = newsArticles

    fun favorite(articles: NewsArticles): Int {
        return newsRepository.favorite(articles)
    }

    fun unfavorite(articles: NewsArticles): Int {
        return newsRepository.unfavorite(articles)
    }
}