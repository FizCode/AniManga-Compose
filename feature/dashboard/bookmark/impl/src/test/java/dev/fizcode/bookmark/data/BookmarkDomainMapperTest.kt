package dev.fizcode.bookmark.data

import dev.fizcode.bookmark.data.mapper.BookmarkDomainMapper
import dev.fizcode.datasource.local.model.BookmarkEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BookmarkDomainMapperTest {

    private val mapper = BookmarkDomainMapper()

    @Test
    fun `null entities map to empty list`() {
        assertTrue(mapper.mapToBookmarkDomainModel(null).isEmpty())
    }

    @Test
    fun `entities keep all fields and order`() {
        val entities = listOf(
            BookmarkEntity(1, "tv", "p1", "8.0", "A", "12 Episodes", "Finished", "Bones", "Action, Comedy"),
            BookmarkEntity(2, "movie", "p2", "9.0", "B", "1 Episode", "Finished", "CoMix", "Drama")
        )

        val domain = mapper.mapToBookmarkDomainModel(entities)

        assertEquals(listOf(1, 2), domain.map { it.mediaId })
        with(domain.first()) {
            assertEquals("tv", mediaType)
            assertEquals("p1", posterPath)
            assertEquals("8.0", rating)
            assertEquals("A", title)
            assertEquals("12 Episodes", episodes)
            assertEquals("Finished", status)
            assertEquals("Bones", studio)
            assertEquals("Action, Comedy", genres)
        }
    }
}
