package com.ahrokholska.gifs.data.network.models

data class GifResponse(
    val data: List<Gif>,
    val pagination: Pagination,
    val meta: Meta,
)