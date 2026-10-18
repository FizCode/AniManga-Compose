package dev.fizcode.bookmark.presentation

import dev.fizcode.bookmark.data.model.BookmarkDomainModel
import dev.fizcode.bookmark.presentation.mapper.BookmarkUiMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BookmarkUiMapperTest {

    private val mapper = BookmarkUiMapper()

    @Test
    fun `empty list maps to empty list`() {
        assertTrue(mapper.mapToBookmarkUiModel(emptyList()).isEmpty())
    }

    @Test
    fun `maps fields, builds subtitle and splits genres`() {
        val ui = mapper.mapToBookmarkUiModel(
            listOf(
                BookmarkDomainModel(
                    mediaId = 1,
                    mediaType = "TV",
                    posterPath = "poster",
                    rating = "8.0",
                    title = "Bleach",
                    episodes = "12 Episodes",
                    status = "Finished",
                    studio = "Pierrot",
                    genres = "Action, Comedy"
                )
            )
        ).single()

        assertEquals(1, ui.mediaId)
        assertEquals("poster", ui.posterPath)
        assertEquals("8.0", ui.rating)
        assertEquals("Bleach", ui.title)
        assertEquals("TV | 12 Episodes | Finished", ui.subTitle)
        assertEquals("Pierrot", ui.studio)
        assertEquals(listOf("Action", "Comedy"), ui.genre.toList())
    }
}
