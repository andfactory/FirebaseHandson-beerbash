package com.hogehoge.newsapp.presentation.summary

import android.arch.lifecycle.*
import android.util.Log
import com.hogehoge.newsapp.model.NewsArticles
import com.hogehoge.newsapp.model.NewsSource
import com.hogehoge.newsapp.repo.NewsRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SummaryViewModel : ViewModel(), LifecycleObserver {

    private val repository = NewsRepository()
    val item = MutableLiveData<ArrayList<NewsArticles>>()

    var queryWord: String = ""

    lateinit var navigator: SummaryNavigator

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        repository.query(queryWord)
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
}