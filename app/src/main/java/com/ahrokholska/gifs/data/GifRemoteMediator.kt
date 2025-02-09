package com.ahrokholska.gifs.data

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
import com.ahrokholska.gifs.domain.useCase.CacheAllGifsUseCase
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

@OptIn(ExperimentalPagingApi::class)
class GifRemoteMediator(
    private val query: String,
    private val database: AppDatabase,
    private val gifService: GifService,
    private val cacheAllGifsUseCase: CacheAllGifsUseCase
) : RemoteMediator<Int, Gif>() {
    private val gifsDao = database.gifDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Gif>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.remoteKeyByQuery(query)
                    }

                    remoteKey.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val limit = state.config.pageSize

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

            val totalCount = response.pagination.totalCount
            val count = response.pagination.count
            val offset = response.pagination.offset
            val isEndOfPaginationReached = totalCount == count + offset

            database.withTransaction {
                remoteKeyDao.insertOrReplace(
                    RemoteKey(
                        label = query,
                        nextKey = if (isEndOfPaginationReached) null else loadKey + count
                    )
                )

                gifsDao.insertAll(response.data.map { it.toEntity() })
                gifsDao.insertAllTags(response.data.map { it.toGifTag(query) })
            }

            cacheAllGifsUseCase(response.data.map { it.id })

            MediatorResult.Success(isEndOfPaginationReached)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }
}