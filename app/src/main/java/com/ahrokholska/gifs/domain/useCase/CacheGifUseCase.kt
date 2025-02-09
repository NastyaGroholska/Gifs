package com.ahrokholska.gifs.domain.useCase

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ahrokholska.gifs.data.work.CacheGifWorker
import javax.inject.Inject

class CacheGifUseCase @Inject constructor(private val workManager: WorkManager) {
    operator fun invoke(id: String) {
        workManager.enqueueUniqueWork(
            uniqueWorkName = id,
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = OneTimeWorkRequestBuilder<CacheGifWorker>()
                .setInputData(workDataOf(CacheGifWorker.ID_PARAM to id))
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
        )
    }
}