package com.app.newsnation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.newsnation.utils.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val sourceId: String?,
    val sourceName: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
)