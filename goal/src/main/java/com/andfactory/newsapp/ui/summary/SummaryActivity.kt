package com.andfactory.newsapp.ui.summary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andfactory.newsapp.R
import com.andfactory.newsapp.adapter.NewsArticlesAdapter
import com.andfactory.newsapp.model.NewsArticles
import com.andfactory.newsapp.ui.top.SummaryViewModel
import com.andfactory.newsapp.utils.getViewModel
import com.andfactory.newsapp.utils.load
import com.andfactory.newsapp.utils.observe
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_summary.*
import kotlinx.android.synthetic.main.empty_layout.*
import kotlinx.android.synthetic.main.progress_layout.*

class SummaryActivity : AppCompatActivity() {

    private val summaryViewModel by lazy { getViewModel<SummaryViewModel>() }
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        private const val KEY_QUERY_TEXT = "query_text"
        fun start(context: Context, queryText: String) {
            val intent = Intent(context, SummaryActivity::class.java)
            intent.putExtra(KEY_QUERY_TEXT, queryText)
            context.startActivity(intent)
        }
    }

    /**
     * Starting point of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val queryText = if (intent.data != null) {
            val uri = intent.data
            uri.getQueryParameter(KEY_QUERY_TEXT)
        } else {
            intent.getStringExtra(KEY_QUERY_TEXT)
        }

        supportActionBar?.let {
            title = queryText
        }

        // Setting up RecyclerView and adapter
        news_list.setEmptyView(empty_view)
        news_list.setProgressView(progress_view)

        val adapter = NewsArticlesAdapter(object : NewsArticlesAdapter.NewsHolder.Listener {
            override fun onItemClick(articles: NewsArticles) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(articles.url)))
            }

            override fun onFavoriteClick(articles: NewsArticles) {
                var favoriteCount: Int
                if (!articles.favorite) {
                    articles.favorite = !articles.favorite
                    favoriteCount = summaryViewModel.favorite(articles)
                } else {
                    articles.favorite = !articles.favorite
                    favoriteCount = summaryViewModel.unfavorite(articles)
                }
            }
        })
        news_list.adapter = adapter
        news_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        // Observing for data change
        summaryViewModel.getNewsArticles().observe(this) {
            it.load(news_list) {
                // Update the UI as the data has changed
                it?.let { adapter.replaceItems(it) }
            }
        }
    }
}