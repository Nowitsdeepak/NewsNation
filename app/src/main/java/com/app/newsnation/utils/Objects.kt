package com.app.newsnation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.model.News

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