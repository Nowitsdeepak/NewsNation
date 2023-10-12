package com.app.newsnation.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.data.local.NewsDao
import com.app.newsnation.data.network.NewsService
import com.app.newsnation.model.News
import com.app.newsnation.utils.Constants
import com.app.newsnation.utils.NetworkUtils
import com.app.newsnation.utils.Objects.asArticleEntities
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val newsService: NewsService,
    private val newsDao: NewsDao,
    private val networkUtils: NetworkUtils,
) {

    private val _newsLiveData = MutableLiveData<List<ArticleEntity>>()
    val newsLiveData: LiveData<List<ArticleEntity>> get() = _newsLiveData

    private val _statusLiveData = MutableLiveData<Constants.STATUS>()
    val statusLiveData: LiveData<Constants.STATUS> get() = _statusLiveData


    suspend fun newsDataManager(category: String, country: String = "in") {

        val categoryLower = category.lowercase()

        if (networkUtils.isConnectedToNetwork()) {

            when (categoryLower) {

                "all" -> {
                    _statusLiveData.value = Constants.STATUS.LOADING
                    val result = newsService.getAllNews(country = country)
                    handleAllNews(result)

                }
                else -> {
                    _statusLiveData.value = Constants.STATUS.LOADING
                    val result = newsService.getByCategory(
                        category = categoryLower, country = country
                    )
                    handleNews(result)
                }
            }
        } else {
            _statusLiveData.value = Constants.STATUS.LOADING
            val news = newsDao.getAllNews()
            news.collect { newsLocal ->
                if (newsLocal.isEmpty()) {
                    _statusLiveData.value = Constants.STATUS.NETWORK_ERROR
                    _newsLiveData.postValue(emptyList())
                }
                _statusLiveData.value = Constants.STATUS.LOADED
                _newsLiveData.postValue(newsLocal)
            }
        }
    }


    private fun handleNews(Result: Response<News>) {
        if (Result.body() != null && Result.isSuccessful) {
            val news = Result.body()!!.asArticleEntities()
            _newsLiveData.postValue(news)
            _statusLiveData.value = Constants.STATUS.LOADED
        } else {
            _statusLiveData.value = Constants.STATUS.EMPTY_LIST
            _newsLiveData.postValue(emptyList())
        }
    }

    private fun handleAllNews(Result: Response<News>) {

        if (Result.body() != null && Result.isSuccessful) {
            val news = Result.body()!!.asArticleEntities()
            _newsLiveData.postValue(news)
//                        newsDao.delete()
//                        newsDao.insert(news)
            _statusLiveData.value = Constants.STATUS.LOADED
//                        TODO(Optimise offline mode)
        } else {
            _statusLiveData.value = Constants.STATUS.EMPTY_LIST
            _newsLiveData.postValue(emptyList())
        }
    }
}