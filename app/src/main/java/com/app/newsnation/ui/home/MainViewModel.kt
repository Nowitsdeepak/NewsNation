package com.app.newsnation.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.repository.MainRepository
import com.app.newsnation.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _currentCategory = MutableLiveData<String>()
    private val currentCategory get() = _currentCategory

    private val _currentSelection = MutableLiveData<ArticleEntity>()
    val currentSelection: LiveData<ArticleEntity> get() = _currentSelection

    init {
        putNews()
    }

    val getNews get() = repository.newsLiveData
    val status get() = repository.statusLiveData

    private fun putNews() {
        viewModelScope.launch {
            repository.newsDataManager(category = currentCategory.value ?: "general")
        }
    }

    fun setCategory(category: String) {
        currentCategory.value = category
    }

    fun refresh() {
        putNews()
    }

    fun setCurrentSelection(news: ArticleEntity) {
        _currentSelection.postValue(news)
    }

    fun markOffline(news: ArticleEntity, isMarked: Boolean) {
        viewModelScope.launch {
            repository.bookmark(news, isMarked)
        }
    }


}