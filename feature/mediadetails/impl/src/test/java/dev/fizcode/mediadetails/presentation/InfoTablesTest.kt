package dev.fizcode.mediadetails.presentation

import dev.fizcode.mediadetails.presentation.info.model.AnimeDataWithLink
import dev.fizcode.mediadetails.presentation.info.model.AnimeInfo
import dev.fizcode.mediadetails.presentation.info.util.buildInformationList
import dev.fizcode.mediadetails.presentation.info.util.buildStatisticsList
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Test

class InfoTablesTest {

    private val info = AnimeInfo(
        type = "TV",
        episodes = "12 Episodes",
        status = "Finished",
        aired = "Apr 2004",
        premiered = "Spring 2004",
        premieredUrl = "premiere-url",
        producers = persistentListOf(AnimeDataWithLink("TV Tokyo", "tt")),
        studios = persistentListOf(AnimeDataWithLink("Pierrot", "p1"), AnimeDataWithLink("Bones", "p2")),
        source = AnimeDataWithLink("Manga", "src"),
        duration = "24 min",
        rating = "PG-13",
        score = "8.0",
        ranked = "#1",
        popularity = "#2",
        members = "3",
        favorites = "4"
    )

    @Test
    fun `statistics list contains five rows in order`() {
        val rows = buildStatisticsList(info)

        assertEquals(
            listOf("Score", "Ranked", "Popularity", "Members", "Favorites"),
            rows.map { it.title }
        )
        assertEquals(listOf("8.0", "#1", "#2", "3", "4"), rows.map { it.desc })
    }

    @Test
    fun `information list maps simple rows`() {
        val rows = buildInformationList(info).associateBy { it.title }

        assertEquals("TV", rows.getValue("Type").desc)
        assertEquals("12 Episodes", rows.getValue("Episodes").desc)
        assertEquals("Finished", rows.getValue("Status").desc)
        assertEquals("24 min", rows.getValue("Duration").desc)
        assertEquals("PG-13", rows.getValue("Rating").desc)
        assertEquals(listOf("premiere-url"), rows.getValue("Premiered").link)
    }

    @Test
    fun `information list maps linked rows`() {
        val rows = buildInformationList(info).associateBy { it.title }

        assertEquals(listOf("Pierrot", "Bones"), rows.getValue("Studios").listDesc)
        assertEquals(listOf("p1", "p2"), rows.getValue("Studios").link)
        assertEquals(listOf("TV Tokyo"), rows.getValue("Producers").listDesc)
        assertEquals("Manga", rows.getValue("Source").desc)
        assertEquals(listOf("src"), rows.getValue("Source").link)
        assertEquals(emptyList<String>(), rows.getValue("Genres").listDesc)
    }
}
