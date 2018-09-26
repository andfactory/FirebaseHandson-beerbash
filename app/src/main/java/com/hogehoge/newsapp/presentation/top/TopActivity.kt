package com.hogehoge.newsapp.presentation.top

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import com.hogehoge.newsapp.R
import com.hogehoge.newsapp.databinding.ActivityTopBinding
import com.hogehoge.newsapp.model.NewsArticles
import com.hogehoge.newsapp.presentation.common.NewsAdapter
import com.hogehoge.newsapp.presentation.summary.SummaryActivity
import com.hogehoge.newsapp.util.extension.getViewModel
import com.hogehoge.newsapp.util.extension.gone
import com.hogehoge.newsapp.util.extension.visible

class TopActivity : AppCompatActivity(), TopNavigator {

    private val viewModel by lazy { getViewModel<TopViewModel>() }

    private val binding: ActivityTopBinding by lazy {
        DataBindingUtil.setContentView<ActivityTopBinding>(this, R.layout.activity_top)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
        viewModel.navigator = this

        setUpSearchView()

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
                            // Todo implement
                            if (!articles.favorite) {
                                articles.favorite = !articles.favorite
                                viewModel.favorite(articles)
                            } else {
                                articles.favorite = !articles.favorite
                                viewModel.unfavorite(articles)
                            }
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

    private fun setUpSearchView() {
        val searchIcon = binding.searchView.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_button)
        searchIcon.setColorFilter(Color.WHITE)

        val searchCloseIcon = binding.searchView.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_close_btn)
        searchCloseIcon.setColorFilter(Color.WHITE)

        val searchAutoComplete = binding.searchView.findViewById<SearchView.SearchAutoComplete>(android.support.v7.appcompat.R.id.search_src_text)
        searchAutoComplete.setHintTextColor(Color.WHITE)
        searchAutoComplete.setTextColor(Color.WHITE)

        val searchPlateEditText = binding.searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
        searchPlateEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                SummaryActivity.start(this, binding.searchView.query.toString())
                true
            }
            false
        }
    }
}
