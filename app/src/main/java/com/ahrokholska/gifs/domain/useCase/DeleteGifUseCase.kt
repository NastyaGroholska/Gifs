package com.ahrokholska.gifs.domain.useCase

import com.ahrokholska.gifs.data.local.dao.GifDao
import javax.inject.Inject

class DeleteGifUseCase @Inject constructor(private val gifDao: GifDao) {
    suspend operator fun invoke(id: String) = gifDao.deleteGif(id)
}