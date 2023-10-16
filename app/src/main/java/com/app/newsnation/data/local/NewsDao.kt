package com.app.newsnation.data.local

import androidx.room.*
import com.app.newsnation.utils.Constants.TABLE_ARTICLE
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: List<ArticleEntity>)

    @Query("SELECT * FROM $TABLE_ARTICLE")
    fun getAllNews(): Flow<List<ArticleEntity>>

    @Query("DELETE FROM $TABLE_ARTICLE")
    suspend fun delete()

    @Update
    suspend fun update(news: ArticleEntity)
}