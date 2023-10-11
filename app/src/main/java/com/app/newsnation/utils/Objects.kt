package com.app.newsnation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.model.News
import com.app.newsnation.utils.Constants.TAG

object Objects {

    fun News.asArticleEntities(): List<ArticleEntity> {
        return articles.map {
            ArticleEntity(
                author = it.author,
                title = it.title,
                content = it.content,
                description = it.description,
                publishedAt = it.publishedAt,
                sourceId = it.source.id,
                sourceName = it.source.name,
                url = it.url,
                urlToImage = it.urlToImage
            )
        }
    }


    fun testToast(context: Context, msg: Any) {
        Toast.makeText(context, "$msg", Toast.LENGTH_SHORT).show()
    }

}