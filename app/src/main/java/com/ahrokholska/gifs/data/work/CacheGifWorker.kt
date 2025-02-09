package com.ahrokholska.gifs.data.work

import android.content.Context
import android.os.Environment
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahrokholska.gifs.data.local.dao.GifDao
import com.ahrokholska.gifs.data.network.GifService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@HiltWorker
class CacheGifWorker @AssistedInject constructor(
    private val gifDao: GifDao,
    private val gifService: GifService,
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val id = inputData.getString(ID_PARAM) ?: return@withContext Result.failure()
            val url = inputData.getString(URL_PARAM) ?: return@withContext Result.failure()

            val gif = gifDao.getGifById(id) ?: return@withContext Result.failure()
            if (gif.localUrl.isNotEmpty()) return@withContext Result.success()


            val response = gifService.downloadFile(url).body()
                ?: return@withContext Result.failure()

            val file = File(
                appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "$IMAGE_PATH/${System.currentTimeMillis()}.gif"
            )
            if (file.parentFile?.exists() == false) {
                if (file.parentFile?.mkdirs() == false) {
                    return@withContext Result.failure()
                }
            }

            FileOutputStream(file).use { output ->
                response.byteStream().use { input ->
                    input.copyTo(output)
                }
            }

            gifDao.updateGifLocal(id, file.path)

            Result.success()
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        const val IMAGE_PATH = "images"
        const val ID_PARAM = "id"
        const val URL_PARAM = "url"
    }
}