package com.ahrokholska.gifs.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahrokholska.gifs.data.local.entities.Gif

@Dao
interface GifDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(gifs: List<Gif>)

    @Query("SELECT * FROM gifs")
    fun pagingSource(): PagingSource<Int, Gif>
}