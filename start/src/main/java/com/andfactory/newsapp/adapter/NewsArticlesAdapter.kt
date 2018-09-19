package com.andfactory.newsapp.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.andfactory.newsapp.R
import com.andfactory.newsapp.model.NewsArticles
import com.andfactory.newsapp.utils.inflate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_news_article.view.*

/**
 * The News adapter to show the news in a list.
 *
 * @author andfactory Chordiya
 * @since 5/23/2017.
 */
class NewsArticlesAdapter(
        private val listener: NewsHolder.Listener
) : RecyclerView.Adapter<NewsArticlesAdapter.NewsHolder>() {

    /**
     * List of news articles
     */
    private var newsArticles: List<NewsArticles> = emptyList()

    /**
     * Inflate the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsHolder(parent.inflate(R.layout.row_news_article))

    /**
     * Bind the view with the data
     */
    override fun onBindViewHolder(newsHolder: NewsHolder, position: Int) = newsHolder.bind(newsArticles[position], listener)

    /**
     * Number of items in the list to display
     */
    override fun getItemCount() = newsArticles.size

    /**
     * View Holder Pattern
     */
    class NewsHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        interface Listener {
            fun onItemClick(articles: NewsArticles)
            fun onFavoriteClick(articles: NewsArticles)
        }

        /**
         * Binds the UI with the data and handles clicks
         */
        fun bind(newsArticle: NewsArticles,
                 listener: Listener) = with(itemView) {
            //news_title.text = newsArticle.title
            //news_description.text = newsArticle.description
            tvNewsItemTitle.text = newsArticle.title
            tvNewsAuthor.text = newsArticle.author
            //TODO: need to format date
            //tvListItemDateTime.text = getFormattedDate(newsArticle.publishedAt)
            tvListItemDateTime.text = newsArticle.publishedAt
            Glide.with(context)
                    .load(newsArticle.urlToImage)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.img_test_one)
                            .error(R.drawable.img_test_one)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivNewsImage)
            setOnClickListener { listener.onItemClick(newsArticle) }
            favorite_button.let {
                if (!newsArticle.favorite) {
                    it.background = ContextCompat.getDrawable(context, R.drawable.selector_no_favorite_button)
                } else {
                    it.background = ContextCompat.getDrawable(context, R.drawable.selector_favorite_button)
                }
                it.setOnClickListener {
                    if (!newsArticle.favorite) {
                        it.background = ContextCompat.getDrawable(context, R.drawable.selector_favorite_button)
                    } else {
                        it.background = ContextCompat.getDrawable(context, R.drawable.selector_no_favorite_button)
                    }
                    listener.onFavoriteClick(newsArticle)
                }
            }
        }
    }

    /**
     * Swap function to set new data on updating
     */
    fun replaceItems(items: List<NewsArticles>) {
        newsArticles = items
        notifyDataSetChanged()
    }
}