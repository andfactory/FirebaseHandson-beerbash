package com.andfactory.newsapp.ui.top

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.andfactory.newsapp.api.NewsSourceService
import com.andfactory.newsapp.db.DatabaseCreator
import com.andfactory.newsapp.model.NewsArticles
import com.andfactory.newsapp.model.network.Resource
import com.andfactory.newsapp.repo.NewsRepository

/**
 * A container for [NewsArticles] related data to show on the UI.
 */
class TopViewModel(application: Application) : AndroidViewModel(application) {

    private var newsArticles: LiveData<Resource<List<NewsArticles>?>>
    private val newsRepository: NewsRepository = NewsRepository(
            DatabaseCreator.database(application).newsArticlesDao(),
            NewsSourceService.getNewsSourceService()
    )

    init {
        // TODO: Inject repository using DI.
        newsArticles = newsRepository.getNewsArticles()
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