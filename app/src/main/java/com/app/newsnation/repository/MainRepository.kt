package com.app.newsnation.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.data.local.NewsDao
import com.app.newsnation.data.network.NewsService
import com.app.newsnation.utils.Constants.TAG
import com.app.newsnation.utils.NetworkUtils
import com.app.newsnation.utils.Objects.asArticleEntities
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val newsService: NewsService,
    private val newsDao: NewsDao,
    private val networkUtils: NetworkUtils,
) {

    private val _newsLiveData = MutableLiveData<List<ArticleEntity>>()

    val newsLiveData: LiveData<List<ArticleEntity>>
        get() = _newsLiveData


    suspend fun newsDataManager(category: String, country: String = "in") {

        val categoryLower = category.lowercase()

        if (networkUtils.isConnectedToNetwork()) {

            when (categoryLower) {

                "all" -> {
                    val result = newsService.getAllNews(country = country)

                    if (result.body() != null && result.isSuccessful) {
                        val news = result.body()!!.asArticleEntities()
                        _newsLiveData.postValue(news)
                        newsDao.delete()
                        newsDao.insert(news)
//                        TODO(Optimise offline mode)
                    } else {
                        _newsLiveData.postValue(emptyList())
                    }
                }
                else -> {
                    Log.d(TAG, "newsDataManagerElse: $categoryLower ")
                    val result = newsService.getByCategory(
                        category = categoryLower,
                        country = country
                    )
                    Log.d(TAG, "newsDataManager: $result")
                    if (result.body() != null && result.isSuccessful) {
                        val news = result.body()!!.asArticleEntities()
                        Log.d(TAG, "newsDataManagerBody: $news")
                        _newsLiveData.postValue(news)
                    }
                }
            }
        } else {
            val news = newsDao.getAllNews()
            news.collect { newsLocal ->
                if (newsLocal.isEmpty()) {
                    _newsLiveData.postValue(emptyList())
                }
                _newsLiveData.postValue(newsLocal)
            }
        }
    }
}