package com.akshay.newsapp.ui.summary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akshay.newsapp.R
import com.akshay.newsapp.adapter.NewsArticlesAdapter
import com.akshay.newsapp.model.NewsArticles
import com.akshay.newsapp.ui.top.SummaryViewModel
import com.akshay.newsapp.ui.top.TopViewModel
import com.akshay.newsapp.utils.getViewModel
import com.akshay.newsapp.utils.load
import com.akshay.newsapp.utils.observe
import com.akshay.newsapp.utils.toast
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_summary.*
import kotlinx.android.synthetic.main.empty_layout.*
import kotlinx.android.synthetic.main.progress_layout.*

class SummaryActivity : AppCompatActivity() {

    private val summaryViewModel by lazy { getViewModel<SummaryViewModel>() }
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        private const val KEY_QUERY_TEXT = "KEY_QUERY_TEXT"
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

        val queryText = intent.getStringExtra(KEY_QUERY_TEXT)
        summaryViewModel.query(queryText)

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