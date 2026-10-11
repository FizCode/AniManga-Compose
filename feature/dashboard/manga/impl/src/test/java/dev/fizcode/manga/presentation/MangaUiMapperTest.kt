package dev.fizcode.manga.presentation

import dev.fizcode.manga.domain.model.MangaDomainModel
import dev.fizcode.manga.presentation.mapper.MangaUiMapper
import dev.fizcode.manga.presentation.model.MangaType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MangaUiMapperTest {

    private val mapper = MangaUiMapper()

    private fun map(item: MangaDomainModel.Item) =
        mapper.mapToMangaUiModel(MangaDomainModel(listOf(item))).single()

    @Test
    fun `known counts and finished status`() {
        val ui = map(
            MangaDomainModel.Item(
                mean = 8.1, mediaType = "light_novel", status = "finished",
                numVolumes = 8, numChapters = 41
            )
        )

        assertEquals("Vol. 8 Ch. 41 | Finished", ui.subTitle)
        assertEquals("Light Novel", ui.mediaTypeLabel)
        assertEquals("8.10", ui.rating)
    }

    @Test
    fun `unknown counts show question marks`() {
        val ui = map(MangaDomainModel.Item(mediaType = "manga", status = "currently_publishing"))

        assertEquals("Vol. ?? Ch. ?? | Publishing", ui.subTitle)
        assertEquals("Manga", ui.mediaTypeLabel)
    }

    @Test
    fun `other tab matches everything except manga, doujinshi and one-shot`() {
        assertTrue(MangaType.OTHER.matches("light_novel"))
        assertTrue(MangaType.OTHER.matches("manhwa"))
        assertFalse(MangaType.OTHER.matches("manga"))
        assertFalse(MangaType.OTHER.matches("doujinshi"))
        assertFalse(MangaType.OTHER.matches("one_shot"))
        assertTrue(MangaType.ALL.matches("anything"))
    }
}
