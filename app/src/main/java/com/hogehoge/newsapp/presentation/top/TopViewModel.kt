package com.hogehoge.newsapp.presentation.top

import android.arch.lifecycle.*
import android.util.Log
import com.hogehoge.newsapp.model.NewsArticles
import com.hogehoge.newsapp.model.NewsSource
import com.hogehoge.newsapp.repo.NewsRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TopViewModel : ViewModel(), LifecycleObserver {

    private val repository = NewsRepository()
    val item = MutableLiveData<ArrayList<NewsArticles>>()

    lateinit var navigator: TopNavigator

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        repository.fetchAllData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<NewsSource> {
                    override fun onSuccess(t: NewsSource) {
                        item.value = t.articles
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Log.e("error", "Error occurred, because $e")
                        navigator.onError()
                    }
                })
    }

    fun favorite(articles: NewsArticles): Int {
        return repository.favorite(articles)
    }

    fun unfavorite(articles: NewsArticles): Int {
        return repository.unfavorite(articles)
    }
}