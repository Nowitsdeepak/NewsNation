package com.app.newsnation.di

import com.app.newsnation.data.network.NewsService
import com.app.newsnation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory
                    .create()
            )
    }

    @Singleton
    @Provides
    fun provideApi(retrofitBuilder: Retrofit.Builder): NewsService =
        retrofitBuilder.build().create(NewsService::class.java)


}