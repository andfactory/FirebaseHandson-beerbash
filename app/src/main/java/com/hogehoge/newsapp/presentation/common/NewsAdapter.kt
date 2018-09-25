package com.hogehoge.newsapp.presentation.common

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hogehoge.newsapp.R
import com.hogehoge.newsapp.databinding.RowNewsArticleBinding
import com.hogehoge.newsapp.model.NewsArticles

class NewsAdapter(private val context: Context,
                  private val items: List<NewsArticles>,
                  private val listener: Listener)
    : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    interface Listener {
        fun onItemClick(articles: NewsArticles)
        fun onFavoriteClick(articles: NewsArticles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.row_news_article,
                        parent,
                        false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            root.setOnClickListener {
                listener.onItemClick(item)
            }
            item.urlToImage?.let {
                Glide.with(context)
                        .load(it)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.img_test_one)
                                .error(R.drawable.img_test_one)
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(ivNewsImage)
            }
            tvNewsItemTitle.text = item.title
            tvNewsAuthor.text = item.author
            tvListItemDateTime.text = item.publishedAt
            favoriteButton.apply {
                if (!item.favorite) {
                    background = ContextCompat.getDrawable(context, R.drawable.selector_no_favorite_button)
                } else {
                    background = ContextCompat.getDrawable(context, R.drawable.selector_favorite_button)
                }
                setOnClickListener {
                    if (!item.favorite) {
                        background = ContextCompat.getDrawable(context, R.drawable.selector_favorite_button)
                    } else {
                        background = ContextCompat.getDrawable(context, R.drawable.selector_no_favorite_button)
                    }
                    listener.onFavoriteClick(item)
                }
            }
        }
    }

    class ViewHolder(val binding: RowNewsArticleBinding) : RecyclerView.ViewHolder(binding.root)
}