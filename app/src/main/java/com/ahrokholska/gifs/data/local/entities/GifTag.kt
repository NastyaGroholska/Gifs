package com.ahrokholska.gifs.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "gif_tags",
    primaryKeys = ["id", "label"],
    foreignKeys = [
        ForeignKey(
            entity = Gif::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        ), ForeignKey(
            entity = RemoteKey::class,
            parentColumns = ["label"],
            childColumns = ["label"],
        )
    ]
)
data class GifTag(
    val id: String,
    val label: String,
)