package com.app.newsnation.data.local

import androidx.room.*
import com.app.newsnation.utils.Constants.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: List<ArticleEntity>)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllNews(): Flow<List<ArticleEntity>>

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun delete()
}