package com.ahrokholska.gifs.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifs")
data class Gif(
    @PrimaryKey val id: String,
    val url: String,
)