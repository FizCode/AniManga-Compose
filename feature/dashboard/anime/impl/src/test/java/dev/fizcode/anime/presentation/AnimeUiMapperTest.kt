package dev.fizcode.anime.presentation

import dev.fizcode.anime.domain.model.Broadcast
import dev.fizcode.anime.domain.model.Data
import dev.fizcode.anime.domain.model.Genre
import dev.fizcode.anime.domain.model.MainPicture
import dev.fizcode.anime.domain.model.Node
import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.model.Studio
import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.presentation.mapper.SeasonalAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopAiringAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopRankingAnimeUiMapper
import dev.fizcode.common.util.extensions.airingStatus
import dev.fizcode.common.util.extensions.animeMediaType
import dev.fizcode.common.util.extensions.releaseInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeasonalAnimeUiMapperTest {

    private val mapper = SeasonalAnimeUiMapper()

    @Test
    fun `empty model maps to empty list`() {
        assertTrue(mapper.mapToSeasonalAnimeUiModel(SeasonalAnimeDomainModel()).isEmpty())
    }

    @Test
    fun `maps node to ui model`() {
        val domain = SeasonalAnimeDomainModel(
            data = listOf(
                Data(
                    Node(
                        id = 7,
                        mediaType = "tv",
                        title = "Frieren",
                        mainPicture = MainPicture(medium = "m", large = "l"),
                        mean = 9.1,
                        broadcast = Broadcast(dayOfTheWeek = "friday", startTime = "23:00"),
                        status = "currently_airing",
                        numEpisodes = 28,
                        studios = listOf(Studio(1, "Madhouse"), Studio(2, "Bones")),
                        synopsis = "A journey",
                        genres = listOf(Genre(1, "Adventure"), Genre(2, "Fantasy"))
                    )
                )
            )
        )

        val ui = mapper.mapToSeasonalAnimeUiModel(domain).single()

        assertEquals(7, ui.id)
        assertEquals("tv", ui.mediaType)
        assertEquals("Frieren", ui.title)
        assertEquals("l", ui.posterPath)
        assertEquals("Madhouse, Bones", ui.studio)
        assertEquals("A journey", ui.synopsis)
        assertEquals("9.1", ui.rating)
        assertEquals(listOf("Adventure", "Fantasy"), ui.genre.toList())
        assertEquals(releaseInfo("friday", "23:00", "currently_airing", 28), ui.releaseInfo)
    }
}

class TopAiringAnimeUiMapperTest {

    private val mapper = TopAiringAnimeUiMapper()

    @Test
    fun `empty model maps to empty list`() {
        assertTrue(mapper.mapToAiringAnimeUiModel(TopAiringDomainModel()).isEmpty())
    }

    @Test
    fun `maps node to ui model`() {
        val domain = TopAiringDomainModel(
            data = listOf(
                TopAiringDomainModel.Data(
                    TopAiringDomainModel.Node(
                        id = 3,
                        mediaType = "tv",
                        title = "One Piece",
                        mainPicture = TopAiringDomainModel.MainPicture(large = "l"),
                        mean = 8.7
                    )
                )
            )
        )

        val ui = mapper.mapToAiringAnimeUiModel(domain).single()

        assertEquals(3, ui.id)
        assertEquals("tv", ui.mediaType)
        assertEquals("One Piece", ui.title)
        assertEquals("l", ui.posterPath)
        assertEquals("8.7", ui.rating)
    }
}

class TopRankingAnimeUiMapperTest {

    private val mapper = TopRankingAnimeUiMapper()

    private fun domain(numEpisodes: Int) = TopRankingDomainModel(
        data = listOf(
            TopRankingDomainModel.Data(
                TopRankingDomainModel.Node(
                    id = 5,
                    mediaType = "movie",
                    title = "Your Name",
                    mainPicture = TopRankingDomainModel.MainPicture(large = "l"),
                    mean = 8.8,
                    status = "finished_airing",
                    numEpisodes = numEpisodes,
                    studios = listOf(TopRankingDomainModel.Studio(1, "CoMix")),
                    genres = listOf(TopRankingDomainModel.Genre(1, "Drama"))
                )
            )
        )
    )

    @Test
    fun `empty model maps to empty list`() {
        assertTrue(mapper.mapToTopRankingAnimeUiModel(TopRankingDomainModel()).isEmpty())
    }

    @Test
    fun `maps node to ui model`() {
        val ui = mapper.mapToTopRankingAnimeUiModel(domain(numEpisodes = 1)).single()

        assertEquals(5, ui.id)
        assertEquals("Your Name", ui.title)
        assertEquals("l", ui.posterPath)
        assertEquals("8.8", ui.rating)
        assertEquals("CoMix", ui.studio)
        assertEquals(listOf("Drama"), ui.genre.toList())
    }

    @Test
    fun `subtitle handles episode counts`() {
        val type = animeMediaType("movie")
        val status = airingStatus("finished_airing")

        assertEquals(
            "$type | $status",
            mapper.mapToTopRankingAnimeUiModel(domain(0)).single().subTitle
        )
        assertEquals(
            "$type | 1 Episode | $status",
            mapper.mapToTopRankingAnimeUiModel(domain(1)).single().subTitle
        )
        assertEquals(
            "$type | 24 Episodes | $status",
            mapper.mapToTopRankingAnimeUiModel(domain(24)).single().subTitle
        )
    }
}
