package com.ahrokholska.gifs.domain.useCase

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ahrokholska.gifs.data.work.StartCacheWorkers
import javax.inject.Inject

class CacheAllGifsUseCase @Inject constructor(private val workManager: WorkManager) {
    operator fun invoke(ids: List<String>) {
        workManager.enqueue(
            request = OneTimeWorkRequestBuilder<StartCacheWorkers>()
                .setInputData(workDataOf(StartCacheWorkers.IDS_PARAM to ids.toTypedArray()))
                .build()
        )
    }
}