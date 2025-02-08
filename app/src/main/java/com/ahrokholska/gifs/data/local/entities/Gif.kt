package com.ahrokholska.gifs.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifs")
data class Gif(
    @PrimaryKey val id: String,
    val networkUrl: String,
    @ColumnInfo(defaultValue = "")
    val localUrl: String = "",
)