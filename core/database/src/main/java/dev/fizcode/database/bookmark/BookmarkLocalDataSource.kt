package dev.fizcode.database.bookmark

import kotlinx.coroutines.flow.Flow

/** The bookmarks saved on this device. */
interface BookmarkLocalDataSource {

    fun isBookmarked(mediaId: Int): Flow<Boolean>

    fun getAllBookmarks(): Flow<List<BookmarkRecord>>

    /** Saves the bookmark, a bookmark of the same media is replaced. Returns the row id. */
    suspend fun saveBookmark(record: BookmarkRecord): Long

    /** Returns how many bookmarks were removed. */
    suspend fun deleteBookmark(mediaId: Int): Int
}
