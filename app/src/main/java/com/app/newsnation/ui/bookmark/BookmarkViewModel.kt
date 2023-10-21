package com.app.newsnation.ui.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.repository.MainRepository
import com.app.newsnation.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val bookmarks = repository.bookmarks


    init {
        bookmarks()
        Log.d(TAG, "Initialized: bookmark")
    }

    private fun bookmarks() {
        viewModelScope.launch {
            repository.getBookmark()
        }
    }

    fun unCheck(news: ArticleEntity, isMarked: Boolean) {
        viewModelScope.launch {
            repository.bookmark(news, isMarked)
        }
    }
}