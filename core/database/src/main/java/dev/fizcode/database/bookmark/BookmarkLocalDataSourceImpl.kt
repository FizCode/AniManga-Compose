package dev.fizcode.database.bookmark

import dev.fizcode.common.settings.AlternativeTitles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BookmarkLocalDataSourceImpl(
    private val bookmarkDao: BookmarkDAO
) : BookmarkLocalDataSource {

    override fun isBookmarked(mediaId: Int): Flow<Boolean> = bookmarkDao.getIsBookmarked(mediaId)

    override fun getAllBookmarks(): Flow<List<BookmarkRecord>> =
        bookmarkDao.getAllBookmarks().map { entities -> entities.orEmpty().map { it.toRecord() } }

    override suspend fun saveBookmark(record: BookmarkRecord): Long =
        bookmarkDao.setBookmark(record.toEntity())

    override suspend fun deleteBookmark(mediaId: Int): Int = bookmarkDao.deleteBookmark(mediaId)

    private fun BookmarkEntity.toRecord() = BookmarkRecord(
        mediaId = mediaId,
        mediaType = mediaType,
        posterPath = posterPath,
        rating = rating,
        title = title,
        episodes = episodes,
        status = status,
        studio = studio,
        genres = decodeGenres(genres),
        alternativeTitles = AlternativeTitles(
            synonyms = decodeSynonyms(titleSynonyms),
            japanese = titleJapanese,
            english = titleEnglish
        )
    )

    private fun BookmarkRecord.toEntity() = BookmarkEntity(
        mediaId = mediaId,
        mediaType = mediaType,
        posterPath = posterPath,
        rating = rating,
        title = title,
        episodes = episodes,
        status = status,
        studio = studio,
        genres = encodeGenres(genres),
        titleSynonyms = encodeSynonyms(alternativeTitles.synonyms),
        titleJapanese = alternativeTitles.japanese,
        titleEnglish = alternativeTitles.english
    )

    // Synonyms share one column, separated by a new line which a title never contains.
    private fun encodeSynonyms(synonyms: List<String>): String =
        synonyms.filter { it.isNotBlank() }.joinToString(SYNONYM_SEPARATOR)

    private fun decodeSynonyms(value: String): List<String> =
        value.split(SYNONYM_SEPARATOR).filter { it.isNotBlank() }

    // Genres share one column, separated by a comma. Rows saved with ", " are read the same way.
    private fun encodeGenres(genres: List<String>): String =
        genres.filter { it.isNotBlank() }.joinToString(GENRE_SEPARATOR)

    private fun decodeGenres(value: String): List<String> =
        value.split(GENRE_SEPARATOR).map { it.trim() }.filter { it.isNotEmpty() }

    private companion object {
        const val GENRE_SEPARATOR = ","
        const val SYNONYM_SEPARATOR = "\n"
    }
}
