package dev.fizcode.search.presentation

import dev.fizcode.common.media.AiringStatus
import dev.fizcode.common.media.AnimeMediaType
import dev.fizcode.search.domain.model.SearchAnimeDomainModel
import dev.fizcode.search.presentation.mapper.SearchUiMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchUiMapperTest {

    private val mapper = SearchUiMapper()

    private fun node(
        mean: Double = 8.0,
        numEpisodes: Int = 12,
        mediaType: String = "tv",
        status: String = "finished_airing"
    ) = SearchAnimeDomainModel.Node(
        id = 1,
        mediaType = mediaType,
        title = "Bleach",
        posterPath = "poster",
        mean = mean,
        status = status,
        numEpisodes = numEpisodes,
        genres = listOf("Action", "Fantasy")
    )

    @Test
    fun `empty domain model maps to empty list`() {
        assertTrue(mapper.mapToSearchResultUiModel(SearchAnimeDomainModel()).isEmpty())
    }

    @Test
    fun `maps basic fields`() {
        val ui = mapper.mapToSearchResultUiModel(SearchAnimeDomainModel(listOf(node()))).single()

        assertEquals(1, ui.id)
        assertEquals("tv", ui.mediaType)
        assertEquals("Bleach", ui.title)
        assertEquals("poster", ui.posterPath)
        assertEquals(listOf("Action", "Fantasy"), ui.genre.toList())
    }

    @Test
    fun `rating is formatted with two decimals`() {
        val ui = mapper.mapToSearchResultUiModel(SearchAnimeDomainModel(listOf(node(mean = 8.1)))).single()
        assertEquals("8.10", ui.rating)
    }

    @Test
    fun `subtitle uses plural episodes`() {
        val ui = mapper.mapToSearchResultUiModel(SearchAnimeDomainModel(listOf(node(numEpisodes = 12)))).single()
        assertEquals("${AnimeMediaType.fromApi("tv").label} | 12 Episodes | ${AiringStatus.fromApi("finished_airing").label}", ui.subTitle)
    }

    @Test
    fun `subtitle uses singular episode`() {
        val ui = mapper.mapToSearchResultUiModel(SearchAnimeDomainModel(listOf(node(numEpisodes = 1)))).single()
        assertEquals("${AnimeMediaType.fromApi("tv").label} | 1 Episode | ${AiringStatus.fromApi("finished_airing").label}", ui.subTitle)
    }

    @Test
    fun `subtitle omits episode count when zero`() {
        val ui = mapper.mapToSearchResultUiModel(SearchAnimeDomainModel(listOf(node(numEpisodes = 0)))).single()
        assertEquals("${AnimeMediaType.fromApi("tv").label} | ${AiringStatus.fromApi("finished_airing").label}", ui.subTitle)
    }
}
