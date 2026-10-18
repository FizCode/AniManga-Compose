package dev.fizcode.database.bookmark

import dev.fizcode.common.settings.AlternativeTitles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BookmarkLocalDataSourceTest {

    private class FakeBookmarkDao : BookmarkDAO {
        val rows = MutableStateFlow<List<BookmarkEntity>>(emptyList())

        override fun getIsBookmarked(mediaId: Int): Flow<Boolean> =
            rows.map { list -> list.any { it.mediaId == mediaId } }

        override fun getAllBookmarks(): Flow<List<BookmarkEntity>?> = rows

        override suspend fun setBookmark(bookmarkEntity: BookmarkEntity): Long {
            rows.value = rows.value.filterNot { it.mediaId == bookmarkEntity.mediaId } + bookmarkEntity
            return bookmarkEntity.mediaId.toLong()
        }

        override suspend fun deleteBookmark(mediaId: Int): Int {
            val before = rows.value.size
            rows.value = rows.value.filterNot { it.mediaId == mediaId }
            return before - rows.value.size
        }
    }

    private val dao = FakeBookmarkDao()
    private val dataSource = BookmarkLocalDataSourceImpl(dao)

    private val record = BookmarkRecord(
        mediaId = 10,
        mediaType = "TV",
        posterPath = "poster",
        rating = "8.5",
        title = "Bleach",
        episodes = "12 Episodes",
        status = "Finished",
        studio = "Pierrot",
        genres = "Action, Comedy",
        alternativeTitles = AlternativeTitles(
            synonyms = listOf("Burichi", "Blich"),
            japanese = "ブリーチ",
            english = "Bleach EN"
        )
    )

    @Test
    fun `a saved bookmark is read back with all its titles`() = runBlocking {
        dataSource.saveBookmark(record)

        assertEquals(listOf(record), dataSource.getAllBookmarks().first())
    }

    @Test
    fun `synonyms are kept in one column and split again`() = runBlocking {
        dataSource.saveBookmark(record)

        assertEquals("Burichi\nBlich", dao.rows.value.single().titleSynonyms)
    }

    @Test
    fun `blank synonyms are left out`() = runBlocking {
        dataSource.saveBookmark(
            record.copy(alternativeTitles = AlternativeTitles(synonyms = listOf("", "A", " ")))
        )

        assertEquals(listOf("A"), dataSource.getAllBookmarks().first().single().alternativeTitles.synonyms)
    }

    @Test
    fun `a bookmark without other titles is read back without them`() = runBlocking {
        dao.rows.value = listOf(
            BookmarkEntity(1, "tv", "p", "8.0", "A", "12 Episodes", "Finished", "Bones", "Action")
        )

        val alternative = dataSource.getAllBookmarks().first().single().alternativeTitles

        assertTrue(alternative.synonyms.isEmpty())
        assertEquals("", alternative.japanese)
        assertEquals("", alternative.english)
    }

    @Test
    fun `saving again replaces the bookmark and deleting removes it`() = runBlocking {
        dataSource.saveBookmark(record)
        dataSource.saveBookmark(record.copy(title = "Bleach 2"))

        assertEquals(listOf("Bleach 2"), dataSource.getAllBookmarks().first().map { it.title })
        assertTrue(dataSource.isBookmarked(10).first())

        assertEquals(1, dataSource.deleteBookmark(10))
        assertFalse(dataSource.isBookmarked(10).first())
        assertTrue(dataSource.getAllBookmarks().first().isEmpty())
    }
}
