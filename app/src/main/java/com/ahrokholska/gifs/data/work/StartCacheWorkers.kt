package com.ahrokholska.gifs.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ahrokholska.gifs.domain.useCase.CacheGifUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StartCacheWorkers @AssistedInject constructor(
    private val cacheGifUseCase: CacheGifUseCase,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val ids = inputData.getStringArray(IDS_PARAM) ?: return Result.failure()
        ids.forEach {
            cacheGifUseCase(it)
        }
        return Result.success()
    }

    companion object {
        const val IDS_PARAM = "ids"
    }
}