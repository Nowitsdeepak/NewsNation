package com.app.newsnation.di

import android.content.Context
import com.app.newsnation.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkUtilsModule {

    @Provides
    fun getNetworkContext(@ApplicationContext appContext: Context): NetworkUtils {
        return NetworkUtils(appContext)
    }
}