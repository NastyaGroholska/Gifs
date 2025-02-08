package com.ahrokholska.gifs.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahrokholska.gifs.data.local.dao.GifDao
import com.ahrokholska.gifs.data.local.entities.Gif

@Database(
    entities = [Gif::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gifDao(): GifDao
}