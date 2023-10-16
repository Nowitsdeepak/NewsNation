package com.app.newsnation.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.newsnation.data.local.bookmark.BookmarkDao
import com.app.newsnation.data.local.bookmark.BookmarkEntity

@Database(
    entities = [ArticleEntity::class, BookmarkEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun bookmarkDao(): BookmarkDao
}