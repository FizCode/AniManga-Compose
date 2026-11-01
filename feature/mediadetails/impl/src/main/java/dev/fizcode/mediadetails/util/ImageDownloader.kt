package dev.fizcode.mediadetails.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.getSystemService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/** How a download ended, as far as [ImageDownloader.download] could tell. */
internal enum class DownloadResult {
    Saved,
    Failed,

    /** Still running (e.g. waiting for network) when we stopped watching, the system goes on. */
    StillRunning
}

/** Saves images into Pictures/AniManga through the system [DownloadManager]. */
internal object ImageDownloader {

    private const val POLL_INTERVAL_MILLIS = 500L
    private const val MAX_WATCH_MILLIS = 60_000L

    /** Below Android 10 the public Pictures folder needs the legacy storage permission. */
    val needsStoragePermission: Boolean
        get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

    /** Queues the download and waits until the system reports how it ended. */
    suspend fun download(context: Context, imageUrl: String): DownloadResult {
        val manager = context.getSystemService<DownloadManager>() ?: return DownloadResult.Failed
        val id = enqueue(manager, imageUrl) ?: return DownloadResult.Failed
        return withContext(Dispatchers.IO) {
            var waited = 0L
            while (waited < MAX_WATCH_MILLIS) {
                when (statusOf(manager, id)) {
                    DownloadManager.STATUS_SUCCESSFUL -> return@withContext DownloadResult.Saved
                    DownloadManager.STATUS_FAILED, null -> return@withContext DownloadResult.Failed
                }
                delay(POLL_INTERVAL_MILLIS)
                waited += POLL_INTERVAL_MILLIS
            }
            DownloadResult.StillRunning
        }
    }

    private fun enqueue(manager: DownloadManager, imageUrl: String): Long? = runCatching {
        val extension = extensionOf(imageUrl)
        val fileName = "${Constant.DOWNLOAD_FOLDER}_${System.currentTimeMillis()}.$extension"
        val request = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle(fileName)
            .setMimeType(mimeTypeOf(extension))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "${Constant.DOWNLOAD_FOLDER}/$fileName"
            )
        manager.enqueue(request)
    }.getOrNull()

    /** The download status, or null when the system no longer knows the download. */
    private fun statusOf(manager: DownloadManager, id: Long): Int? =
        runCatching {
            manager.query(DownloadManager.Query().setFilterById(id)).use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                } else {
                    null
                }
            }
        }.getOrNull()

    private fun extensionOf(url: String): String =
        url.substringBefore('?').substringAfterLast('.', "jpg").lowercase()
            .takeIf { it in SUPPORTED_EXTENSIONS } ?: "jpg"

    private fun mimeTypeOf(extension: String) = when (extension) {
        "png" -> "image/png"
        "webp" -> "image/webp"
        else -> "image/jpeg"
    }

    private val SUPPORTED_EXTENSIONS = setOf("jpg", "jpeg", "png", "webp")
}
