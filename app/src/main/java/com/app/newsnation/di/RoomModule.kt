package com.app.newsnation.di

import android.content.Context
import androidx.room.Room
import com.app.newsnation.data.local.NewsDao
import com.app.newsnation.data.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): NewsDatabase {
        return Room.databaseBuilder(
            appContext,
            NewsDatabase::class.java,
            "news_nation.db"
        ).build()
    }

    @Provides
    fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao {
        return newsDatabase.newsDao()
    }

}