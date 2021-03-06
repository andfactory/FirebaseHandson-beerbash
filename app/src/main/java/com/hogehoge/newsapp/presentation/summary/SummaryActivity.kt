package com.hogehoge.newsapp.presentation.summary

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hogehoge.newsapp.R
import com.hogehoge.newsapp.databinding.ActivitySummaryBinding
import com.hogehoge.newsapp.model.NewsArticles
import com.hogehoge.newsapp.presentation.common.NewsAdapter
import com.hogehoge.newsapp.util.extension.getViewModel
import com.hogehoge.newsapp.util.extension.gone
import com.hogehoge.newsapp.util.extension.visible

class SummaryActivity : AppCompatActivity(), SummaryNavigator {

    private val viewModel by lazy { getViewModel<SummaryViewModel>() }
    private val binding: ActivitySummaryBinding by lazy {
        DataBindingUtil.setContentView<ActivitySummaryBinding>(this, R.layout.activity_summary)
    }

    private lateinit var queryWord: String

    companion object {
        private const val KEY_QUERY_TEXT = "query_text"
        fun start(context: Context, queryText: String) {
            val intent = Intent(context, SummaryActivity::class.java)
            intent.putExtra(KEY_QUERY_TEXT, queryText)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
        viewModel.navigator = this

        queryWord = if (intent.data != null) {
            intent.data.getQueryParameter(KEY_QUERY_TEXT)
        } else {
            intent.getStringExtra(KEY_QUERY_TEXT)
        }

        viewModel.queryWord = queryWord
        supportActionBar?.let {
            title = queryWord
        }

        viewModel.item.observe(this, Observer {
            if (it == null || it.size == 0) {
                binding.emptyView?.visible()
                binding.progress?.gone()
            } else {
                binding.newsList.apply {
                    val listener = object : NewsAdapter.Listener {
                        override fun onItemClick(articles: NewsArticles) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(articles.url)))
                        }

                        override fun onFavoriteClick(articles: NewsArticles) {

                        }
                    }
                    adapter = NewsAdapter(context, it, listener)
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                }
                binding.progress?.gone()
            }
        })
    }

    override fun onError() {
        binding.progress?.gone()
        binding.emptyView?.visible()
    }
}