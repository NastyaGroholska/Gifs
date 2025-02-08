package com.ahrokholska.gifs.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahrokholska.gifs.data.local.entities.Gif
import com.ahrokholska.gifs.data.local.entities.GifTag

@Dao
interface GifDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(gifs: List<Gif>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllTags(gifTags: List<GifTag>)

    @Query("SELECT * FROM gifs LEFT  JOIN gif_tags ON gifs.id = gif_tags.id  WHERE label=:query")
    fun pagingSource(query: String): PagingSource<Int, Gif>

    @Query("SELECT * FROM gifs WHERE id=:id")
    suspend fun getGifById(id: String): Gif?

    @Query("UPDATE gifs SET localUrl=:localUrl WHERE id=:id")
    suspend fun updateGifLocal(id: String, localUrl: String)
}