package com.ahrokholska.gifs.domain.useCase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.ahrokholska.gifs.data.Constants.PAGE_SIZE
import com.ahrokholska.gifs.data.GifRemoteMediator
import com.ahrokholska.gifs.data.local.AppDatabase
import com.ahrokholska.gifs.data.local.dao.GifDao
import com.ahrokholska.gifs.data.mappers.toDomain
import com.ahrokholska.gifs.data.network.GifService
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPaginatedDataUseCase @Inject constructor(
    private val database: AppDatabase,
    private val gifsDao: GifDao,
    private val gifService: GifService,
) {
    @OptIn(ExperimentalPagingApi::class)
    operator fun invoke(query: String) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        remoteMediator = GifRemoteMediator(query, database, gifService)
    ) {
        gifsDao.pagingSource(query)
    }.flow.map { pagingData ->
        pagingData.map {
            it.toDomain()
        }
    }
}