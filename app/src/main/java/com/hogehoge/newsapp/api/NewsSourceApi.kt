package com.hogehoge.newsapp.api

import com.hogehoge.newsapp.BuildConfig
import com.hogehoge.newsapp.model.NewsSource
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsSourceApi {

    @GET("top-headlines?country=us&apiKey=" + BuildConfig.NEWS_API_KEY)
    fun getNewsSource(): Single<NewsSource>

    @GET("top-headlines?apiKey=" + BuildConfig.NEWS_API_KEY)
    fun query(@Query("q") word: String): Single<NewsSource>

    companion object {
        private const val BASE_URL = "https://newsapi.org/v2/"

        fun getNewsSourceService(): NewsSourceApi {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(NewsSourceApi::class.java)
        }
    }
}