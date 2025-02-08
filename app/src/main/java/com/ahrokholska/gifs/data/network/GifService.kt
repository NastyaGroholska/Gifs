package com.ahrokholska.gifs.data.network

import com.ahrokholska.gifs.data.network.models.GifResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GifService {
    @GET("/v1/gifs/search")
    suspend fun getGifs(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("q") query: String,
        @Query("bundle") bundle: String = "messaging_non_clips",
    ): GifResponse

    @GET("/v1/gifs/trending")
    suspend fun getTrendingGifs(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("bundle") bundle: String = "messaging_non_clips",
    ): GifResponse
}