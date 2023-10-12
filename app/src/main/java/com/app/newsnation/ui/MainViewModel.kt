package com.app.newsnation.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.newsnation.repository.MainRepository
import com.app.newsnation.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _currentCategory = MutableLiveData<String>()
    private val currentCategory get() = _currentCategory

    init {
        setCategory("all")
        getNews()
    }

    val getNews get() = repository.newsLiveData
    val status get() = repository.statusLiveData

    private fun getNews() {
        viewModelScope.launch {
            repository.newsDataManager(category = currentCategory.value ?: "all")
        }
    }

    fun setCategory(category: String) {
        Log.d(TAG, "putCategory: $category")
        currentCategory.value = category
    }

    fun refresh() {
        getNews()
        Log.d(TAG, "refresh: News Refreshed Called")
    }

}