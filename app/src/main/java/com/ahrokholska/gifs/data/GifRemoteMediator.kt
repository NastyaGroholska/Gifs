package com.ahrokholska.gifs.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ahrokholska.gifs.data.local.AppDatabase
import com.ahrokholska.gifs.data.local.entities.Gif
import com.ahrokholska.gifs.data.local.entities.RemoteKey
import com.ahrokholska.gifs.data.mappers.toEntity
import com.ahrokholska.gifs.data.mappers.toGifTag
import com.ahrokholska.gifs.data.network.GifService
import com.ahrokholska.gifs.domain.useCase.CacheGifUseCase
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

@OptIn(ExperimentalPagingApi::class)
class GifRemoteMediator(
    private val query: String,
    private val database: AppDatabase,
    private val gifService: GifService,
    private val cacheGifUseCase: CacheGifUseCase
) : RemoteMediator<Int, Gif>() {
    private val gifsDao = database.gifDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Gif>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0.also { Log.d("WWW", "REFRESH") }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                    .also { Log.d("WWW", "PREPEND") }

                LoadType.APPEND -> {
                    Log.d("WWW", "APPEND")
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.remoteKeyByQuery(query)
                    }
                    Log.d("WWW", "remoteKey $remoteKey")

                    if (remoteKey.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextKey
                }
            }

            val limit = state.config.pageSize
            Log.d("WWW", "network limit=$limit offset=$loadKey")

            val response = if (query.isEmpty()) {
                gifService.getTrendingGifs(
                    limit = limit,
                    offset = loadKey,
                )
            } else {
                gifService.getGifs(
                    limit = limit,
                    offset = loadKey,
                    query = query
                )
            }

            Log.d("WWW", "result $response")

            val totalCount = response.pagination.totalCount
            val count = response.pagination.count
            val offset = response.pagination.offset
            val isEndOfPaginationReached = totalCount == count + offset

            Log.d("WWW", "loadKey + count ${loadKey + count}")

            database.withTransaction {
                remoteKeyDao.insertOrReplace(
                    RemoteKey(
                        label = query,
                        nextKey = if (isEndOfPaginationReached
                            || loadKey + count == 3
                        ) null else loadKey + count
                    )
                )

                gifsDao.insertAll(response.data.map { it.toEntity() })
                gifsDao.insertAllTags(response.data.map { it.toGifTag(query) })
            }

            response.data.forEach {
                cacheGifUseCase(it.id, it.images.original.url)
            }

            MediatorResult.Success(isEndOfPaginationReached)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }
}