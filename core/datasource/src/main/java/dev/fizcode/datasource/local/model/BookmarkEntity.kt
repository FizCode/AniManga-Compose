package dev.fizcode.datasource.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.fizcode.datasource.utils.Constant

@Entity(tableName = Constant.BOOKMARK_TABLE)
data class BookmarkEntity(
    @PrimaryKey @ColumnInfo(name = "media_id") val mediaId: Int,
    @ColumnInfo(name = "media_type") val mediaType: String,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    @ColumnInfo(name = "rating") val rating: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "episodes") val episodes: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "studio") val studio: String,
    @ColumnInfo(name = "genres") val genres: String
)
