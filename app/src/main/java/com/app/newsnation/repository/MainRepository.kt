package com.app.newsnation.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.data.local.NewsDao
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
) {

    private val _newsLiveData = MutableLiveData<List<ArticleEntity>>()
    val newsLiveData: LiveData<List<ArticleEntity>> get() = _newsLiveData

    private val _statusLiveData = MutableLiveData<Constants.STATUS>()
    val statusLiveData: LiveData<Constants.STATUS> get() = _statusLiveData

    private val _bookmarks = MutableLiveData<List<ArticleEntity>>()
    val bookmarks: LiveData<List<ArticleEntity>> = _bookmarks


    suspend fun newsDataManager(category: String, country: String = "in") {

        val categoryLower: String = if (category == "All") {
            "general"
        } else {
            category.lowercase()
        }

        Log.d(TAG, "newsDataManager: $categoryLower")

        try {
            if (networkUtils.isConnectedToNetwork()) {
                if (categoryLower == "general") newsDao.delete()
            }

            _statusLiveData.value = Constants.STATUS.LOADING
            val result = newsService.getByCategory(category = categoryLower, country = country)
            Log.d(TAG, "newsDataManager: $result")
            handleNews(result)

        } catch (e: Exception) {

            Log.d(TAG, "newsDataManager: $e")

            val news = newsDao.getAllNews()
            news.collect { newsLocal ->
                if (newsLocal.isEmpty()) {
                    Log.d(TAG, "newsDataManagerNewsLocal: $newsLocal")
                    _statusLiveData.value = Constants.STATUS.NETWORK_ERROR
                }
                val processed = processData(newsLocal)
                _newsLiveData.postValue(processed)
                _statusLiveData.value = Constants.STATUS.LOADED
            }
        }
    }

    private suspend fun handleNews(Result: Response<News>) {
        if (Result.body() != null && Result.isSuccessful) {
            val news = Result.body()!!.asArticleEntities()
            val processed = processData(news)
            _newsLiveData.postValue(processed)
            newsDao.insert(news)
            _statusLiveData.value = Constants.STATUS.LOADED
        }
    }


    private suspend fun processData(
        result: List<ArticleEntity>,
    ): List<ArticleEntity> {
        try {
            val bookmarks = newsDao.getBookmarks().first()
            if (bookmarks.isNotEmpty()) {
                result.forEach { news ->
                    val isMarked = bookmarks.any { it.title == news.title }
                    news.isBookmark = isMarked
                    Log.d(TAG, "processData: $news")
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "processData: $e")
        }
        return result
    }

    suspend fun getBookmark() {
        newsDao.getBookmarks().collect { bookmarks ->
            Log.d(TAG, "getBookmark: $bookmarks")
            _bookmarks.postValue(bookmarks)
        }
    }

    suspend fun bookmark(news: ArticleEntity, isMarked: Boolean) {
        val toggleMark = news.copy(isBookmark = isMarked)
        Log.d(TAG, "toggled: ${news.isBookmark},${toggleMark.isBookmark}")
        newsDao.update(toggleMark)
        Log.d(TAG, "bookmark: DataUpdated")
    }
}