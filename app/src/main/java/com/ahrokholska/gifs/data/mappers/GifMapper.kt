package com.ahrokholska.gifs.data.mappers

import com.ahrokholska.gifs.data.local.entities.GifTag
import com.ahrokholska.gifs.data.network.models.Gif
import com.ahrokholska.gifs.data.local.entities.Gif as GifEntity

fun Gif.toEntity() = GifEntity(
    id = id,
    url = images.original.url,
)

fun Gif.toGifTag(label: String) = GifTag(
    id = id,
    label = label
)