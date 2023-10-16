package com.app.newsnation.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.data.local.NewsDao
import com.app.newsnation.data.local.bookmark.BookmarkDao
import com.app.newsnation.data.network.NewsService
import com.app.newsnation.model.News
import com.app.newsnation.utils.Constants
import com.app.newsnation.utils.Constants.TAG
import com.app.newsnation.utils.NetworkUtils
import com.app.newsnation.utils.Objects.asArticleEntities
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val newsService: NewsService,
    private val newsDao: NewsDao,
    private val networkUtils: NetworkUtils,
    private val bookmarkDao: BookmarkDao,
) {

    private val _newsLiveData = MutableLiveData<List<ArticleEntity>>()
    val newsLiveData: LiveData<List<ArticleEntity>> get() = _newsLiveData

    private val _statusLiveData = MutableLiveData<Constants.STATUS>()
    val statusLiveData: LiveData<Constants.STATUS> get() = _statusLiveData


    suspend fun newsDataManager(category: String, country: String = "in") {

        val categoryLower = category.lowercase()

        try {

            if (networkUtils.isConnectedToNetwork()) {

                when (categoryLower) {

                    "all" -> {
                        _statusLiveData.value = Constants.STATUS.LOADING
                        val result = newsService.getAllNews(country = country)
                        Log.d(TAG, "newsDataManager: $result")
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
                val news = newsDao.getAllNews()
                news.collect { newsLocal ->
                    if (newsLocal.isEmpty()) {
                        _newsLiveData.postValue(emptyList())
                    }
                    _statusLiveData.value = Constants.STATUS.LOADED
                    _newsLiveData.postValue(newsLocal)
                }
            }
        } catch (e: Exception) {
            _statusLiveData.value = Constants.STATUS.LOADED
            Log.d(TAG, "newsDataManager: TimeOut")
        }
    }

    private suspend fun handleNews(Result: Response<News>) {
        if (Result.body() != null && Result.isSuccessful) {
            val news = Result.body()!!.asArticleEntities()
            val processed = processData(news)
            _newsLiveData.postValue(processed)
        } else {
            _newsLiveData.postValue(emptyList())
        }
    }

    private suspend fun handleAllNews(Result: Response<News>) {

        if (Result.body() != null && Result.isSuccessful) {
            val news = Result.body()!!.asArticleEntities()
            val processed = processData(news)
            _newsLiveData.postValue(processed)
            newsDao.delete()
            newsDao.insert(processed)

            Log.d(TAG, "handleAllNews: ")
//                        TODO(Optimise offline mode)
        } else {
            _newsLiveData.postValue(emptyList())
        }
    }

    private suspend fun processData(result: List<ArticleEntity>): List<ArticleEntity> {
        try {
            val bookmarks = bookmarkDao.getBookmarks().first()

            result.forEach { news ->

                val isMarked = bookmarks.any { it.title == news.title }

                news.isBookmark = isMarked
            }
        } catch (e: Exception) {
            Log.d(TAG, "processData: $e")
        }
        return result
    }

    suspend fun markOffline(news: ArticleEntity, isMarked: Boolean) {
        val toggleMark = news.copy(isBookmark = isMarked)
        Log.d(TAG, "markOffline: ${news.isBookmark},${toggleMark.isBookmark}")
        newsDao.update(toggleMark)
    }

}