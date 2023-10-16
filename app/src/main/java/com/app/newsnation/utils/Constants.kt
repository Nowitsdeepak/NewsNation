package com.app.newsnation.utils

import com.app.newsnation.model.Category

object Constants {

    const val TAG = "MY_TAG"

    const val BASE_URL = "https://newsapi.org/"
    const val API_KEY = "f2b1c555c06546a7a0fb10b90de52084"
    const val TABLE_ARTICLE = "articles"
    const val TABLE_BOOKMARK = "bookmarks"

    val clist = listOf<Category>(
        Category("All"),
        Category("Entertainment"),
        Category("Business"),
        Category("Health"),
        Category("Science"),
        Category("Sports"),
        Category("Technology"),
    )

    enum class STATUS {
        NETWORK_ERROR,
        LOADING,
        LOADED,
        EMPTY_LIST
    }
}