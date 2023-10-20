package com.app.newsnation.data.network

import com.app.newsnation.model.News
import com.app.newsnation.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsService {
//Add your api key from news.org
    @GET("v2/top-headlines?apiKey=$API_KEY")
    suspend fun getAllNews(@Query("country") country: String = "in"): Response<News>

    @GET("v2/top-headlines?apiKey=$API_KEY")
    suspend fun getByCategory(
        @Query("country") country: String = "in",
        @Query("category") category: String,
    ): Response<News>

}