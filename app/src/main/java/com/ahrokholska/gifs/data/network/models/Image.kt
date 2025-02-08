package com.ahrokholska.gifs.data.network.models

data class Image(
    val original: OriginalImage
)

data class OriginalImage(
    val url: String
)