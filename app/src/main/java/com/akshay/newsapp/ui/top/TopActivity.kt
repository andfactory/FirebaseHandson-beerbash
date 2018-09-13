package com.akshay.newsapp.ui.top

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.akshay.newsapp.R
import com.akshay.newsapp.adapter.NewsArticlesAdapter
import com.akshay.newsapp.model.NewsArticles
import com.akshay.newsapp.ui.summary.SummaryActivity
import com.akshay.newsapp.utils.getViewModel
import com.akshay.newsapp.utils.load
import com.akshay.newsapp.utils.observe
import com.akshay.newsapp.utils.toast
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_top.*
import kotlinx.android.synthetic.main.empty_layout.*
import kotlinx.android.synthetic.main.progress_layout.*

/**
 * The Main or Starting Activity.
 *
 * @author Akshay Chordiya
 * @since 5/23/2017.
 */
class TopActivity : AppCompatActivity() {

    private val topViewModel by lazy { getViewModel<TopViewModel>() }
    private lateinit var fibaseAnalytics: FirebaseAnalytics

    /**
     * Starting point of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        fibaseAnalytics = FirebaseAnalytics.getInstance(this)

        val searchView = findViewById<SearchView>(R.id.search_view)
        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchIcon.setColorFilter(Color.WHITE)

        val searchCloseIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        searchCloseIcon.setColorFilter(Color.WHITE)

        val searchAutoComplete = searchView.findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete.setHintTextColor(Color.WHITE)
        searchAutoComplete.setTextColor(Color.WHITE)

        val searchPlateEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchPlateEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val intent = Intent(this@TopActivity, SummaryActivity::class.java)
                intent.putExtra("query", searchView.query.toString())
                startActivity(intent)
                true
            }
            false
        }

        // Setting up RecyclerView and adapter
        news_list.setEmptyView(empty_view)
        news_list.setProgressView(progress_view)

        val adapter = NewsArticlesAdapter(object : NewsArticlesAdapter.NewsHolder.Listener {
            override fun onItemClick(articles: NewsArticles) {
                toast("Clicked on item")
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(articles.url)))
            }

            override fun onFavoriteClick(articles: NewsArticles) {
                var favoriteCount: Int
                if (!articles.favorite) {
                    articles.favorite = !articles.favorite
                    favoriteCount = topViewModel.favorite(articles)
                } else {
                    articles.favorite = !articles.favorite
                    favoriteCount = topViewModel.unfavorite(articles)
                }
                if (favoriteCount == 5) {
                    val bundle = Bundle()
                    fibaseAnalytics.logEvent("play_store", bundle)
                } else {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, articles.id.toString())
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, articles.title)
                    fibaseAnalytics.logEvent("suggest_article", bundle)
                }
            }
        })
        news_list.adapter = adapter
        news_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        // Observing for data change
        topViewModel.getNewsArticles().observe(this) {
            it.load(news_list) {
                // Update the UI as the data has changed
                it?.let { adapter.replaceItems(it) }
            }
        }
    }
}
