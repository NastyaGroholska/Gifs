package com.ahrokholska.gifs.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahrokholska.gifs.data.local.dao.GifDao
import com.ahrokholska.gifs.data.local.dao.RemoteKeyDao
import com.ahrokholska.gifs.data.local.entities.Gif
import com.ahrokholska.gifs.data.local.entities.GifTag
import com.ahrokholska.gifs.data.local.entities.RemoteKey

@Database(
    entities = [Gif::class, RemoteKey::class, GifTag::class],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 3,
            to = 4,
            spec = AppDatabase.AutoMigration_3_4::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gifDao(): GifDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    @RenameColumn(tableName = "gifs", "url", "networkUrl")
    class AutoMigration_3_4 : AutoMigrationSpec

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS remote_keys (`label` TEXT NOT NULL, `nextKey` INTEGER, " +
                            "PRIMARY KEY(`label`))"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS gif_tags (" +
                            "`id` TEXT NOT NULL  REFERENCES `gifs`(`id`) ON DELETE CASCADE," +
                            "`label` TEXT NOT NULL  REFERENCES `remote_keys`(`label`), " +
                            "PRIMARY KEY(`id`,`label`))"
                )
            }
        }
    }
}