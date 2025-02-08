package com.ahrokholska.gifs.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahrokholska.gifs.data.local.dao.GifDao
import com.ahrokholska.gifs.data.local.dao.RemoteKeyDao
import com.ahrokholska.gifs.data.local.entities.Gif
import com.ahrokholska.gifs.data.local.entities.RemoteKey

@Database(
    entities = [Gif::class, RemoteKey::class],
    version = 2,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gifDao(): GifDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS remote_keys (`label` TEXT NOT NULL, `nextKey` INTEGER, " +
                            "PRIMARY KEY(`label`))"
                )
            }
        }
    }
}