package com.ahrokholska.gifs.data.work

import android.content.Context
import android.os.Environment
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahrokholska.gifs.data.local.dao.GifDao
import com.bumptech.glide.Glide
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@HiltWorker
class CacheGifWorker @AssistedInject constructor(
    private val gifDao: GifDao,
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val id = inputData.getString(ID_PARAM) ?: return@withContext Result.failure()
        val url = inputData.getString(URL_PARAM) ?: return@withContext Result.failure()

        val gif = gifDao.getGifById(id) ?: return@withContext Result.failure()
        if (gif.localUrl.isNotEmpty()) return@withContext Result.success()

        val downloadedFile = Glide.with(appContext)
            .download(url)
            .submit()
            .get()

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
            output.write(downloadedFile.readBytes())
        }

        gifDao.updateGifLocal(id, file.path)

        Result.success()
    }

    companion object {
        const val IMAGE_PATH = "images"
        const val ID_PARAM = "id"
        const val URL_PARAM = "url"
    }
}