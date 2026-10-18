package dev.fizcode.database.bookmark

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.fizcode.database.util.Constant
import kotlinx.coroutines.flow.Flow

@Dao
internal interface BookmarkDAO {

    @Query("SELECT EXISTS(SELECT 1 FROM ${Constant.BOOKMARK_TABLE} WHERE media_id = :mediaId)")
    fun getIsBookmarked(mediaId: Int): Flow<Boolean>

    @Query("SELECT * FROM ${Constant.BOOKMARK_TABLE}")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setBookmark(bookmarkEntity: BookmarkEntity): Long

    @Query("DELETE FROM ${Constant.BOOKMARK_TABLE} WHERE media_id = :mediaId")
    suspend fun deleteBookmark(mediaId: Int): Int
}
