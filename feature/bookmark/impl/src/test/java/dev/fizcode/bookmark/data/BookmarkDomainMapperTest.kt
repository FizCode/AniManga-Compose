package dev.fizcode.bookmark.data

import dev.fizcode.bookmark.data.mapper.BookmarkDomainMapper
import dev.fizcode.common.settings.AlternativeTitles
import dev.fizcode.database.bookmark.BookmarkRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BookmarkDomainMapperTest {

    private val mapper = BookmarkDomainMapper()

    @Test
    fun `no records map to an empty list`() {
        assertTrue(mapper.mapToBookmarkDomainModel(emptyList()).isEmpty())
    }

    @Test
    fun `records keep all fields and order`() {
        val records = listOf(
            BookmarkRecord(1, "tv", "p1", "8.0", "A", "12 Episodes", "Finished", "Bones", listOf("Action", "Comedy")),
            BookmarkRecord(2, "movie", "p2", "9.0", "B", "1 Episode", "Finished", "CoMix", listOf("Drama"))
        )

        val domain = mapper.mapToBookmarkDomainModel(records)

        assertEquals(listOf(1, 2), domain.map { it.mediaId })
        with(domain.first()) {
            assertEquals("tv", mediaType)
            assertEquals("p1", posterPath)
            assertEquals("8.0", rating)
            assertEquals("A", title)
            assertEquals("12 Episodes", episodes)
            assertEquals("Finished", status)
            assertEquals("Bones", studio)
            assertEquals(listOf("Action", "Comedy"), genres)
        }
    }

    @Test
    fun `alternative titles are kept`() {
        val record = BookmarkRecord(
            1, "tv", "p1", "8.0", "A", "12 Episodes", "Finished", "Bones", listOf("Action"),
            alternativeTitles = AlternativeTitles(listOf("A1", "A2"), japanese = "エー", english = "Ay")
        )

        val alternative = mapper.mapToBookmarkDomainModel(listOf(record)).single().alternativeTitles

        assertEquals(listOf("A1", "A2"), alternative.synonyms)
        assertEquals("エー", alternative.japanese)
        assertEquals("Ay", alternative.english)
    }

    @Test
    fun `a bookmark without other titles keeps only the default title`() {
        val record = BookmarkRecord(1, "tv", "p1", "8.0", "A", "12 Episodes", "Finished", "Bones", listOf("Action"))

        val alternative = mapper.mapToBookmarkDomainModel(listOf(record)).single().alternativeTitles

        assertEquals(AlternativeTitles(), alternative)
    }
}
