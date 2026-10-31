package dev.fizcode.database.bookmark

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.fizcode.database.util.Constant

@Entity(tableName = Constant.BOOKMARK_TABLE)
internal data class BookmarkEntity(
    @PrimaryKey @ColumnInfo(name = "media_id") val mediaId: Int,
    @ColumnInfo(name = "media_type") val mediaType: String,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    @ColumnInfo(name = "rating") val rating: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "episodes") val episodes: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "studio") val studio: String,
    /** The genres in one column, see [BookmarkLocalDataSourceImpl]. */
    @ColumnInfo(name = "genres") val genres: String,
    /** The synonyms in one column, see [BookmarkLocalDataSourceImpl]. */
    @ColumnInfo(name = "title_synonyms", defaultValue = "''") val titleSynonyms: String = "",
    @ColumnInfo(name = "title_japanese", defaultValue = "''") val titleJapanese: String = "",
    @ColumnInfo(name = "title_english", defaultValue = "''") val titleEnglish: String = ""
)
