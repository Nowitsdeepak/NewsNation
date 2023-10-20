package com.app.newsnation.data.local

import androidx.room.*
import com.app.newsnation.utils.Constants.TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: List<ArticleEntity>)

    @Query("SELECT * FROM $TABLE")
    fun getAllNews(): Flow<List<ArticleEntity>>

    @Query("DELETE FROM $TABLE WHERE isBookmark = 0")
    suspend fun delete()

    @Update
    suspend fun update(news: ArticleEntity)

    @Query("SELECT * FROM $TABLE WHERE isBookmark = 1 ")
    fun getBookmarks(): Flow<List<ArticleEntity>>

}