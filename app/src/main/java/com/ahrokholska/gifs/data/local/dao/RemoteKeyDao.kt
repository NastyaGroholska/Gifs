package com.ahrokholska.gifs.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahrokholska.gifs.data.local.entities.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKey)

    @Query("SELECT * FROM remote_keys WHERE label = :query")
    suspend fun remoteKeyByQuery(query: String): RemoteKey

    @Query("DELETE FROM remote_keys WHERE label = :query")
    suspend fun deleteByQuery(query: String)
}