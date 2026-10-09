package dev.fizcode.anime.data

import dev.fizcode.anime.data.mapper.SeasonalDomainMapper
import dev.fizcode.anime.data.mapper.TopAiringDomainMapper
import dev.fizcode.anime.data.mapper.TopRankingDomainMapper
import dev.fizcode.datasource.remote.response.CurrentSeasonAnimeResponse
import dev.fizcode.datasource.remote.response.TopAiringAnimeResponse
import dev.fizcode.datasource.remote.response.TopRankingResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeasonalDomainMapperTest {

    private val mapper = SeasonalDomainMapper()

    @Test
    fun `empty response maps to defaults`() {
        val domain = mapper.mapToSeasonAnime(CurrentSeasonAnimeResponse())

        assertTrue(domain.data.isEmpty())
        assertEquals("", domain.paging.next)
        assertEquals(0, domain.season.year)
        assertEquals("", domain.season.season)
    }

    @Test
    fun `null node maps to default node`() {
        val domain = mapper.mapToSeasonAnime(
            CurrentSeasonAnimeResponse(data = listOf(CurrentSeasonAnimeResponse.Data(null)))
        )

        assertEquals(0, domain.data.single().node.id)
        assertEquals("", domain.data.single().node.title)
    }

    @Test
    fun `maps a full response`() {
        val domain = mapper.mapToSeasonAnime(
            CurrentSeasonAnimeResponse(
                data = listOf(
                    CurrentSeasonAnimeResponse.Data(
                        CurrentSeasonAnimeResponse.Node(
                            id = 1,
                            title = "Title",
                            mainPicture = CurrentSeasonAnimeResponse.MainPicture("m", "l"),
                            mean = 7.5,
                            status = "currently_airing",
                            mediaType = "tv",
                            numEpisodes = 12,
                            studios = listOf(CurrentSeasonAnimeResponse.Studio(2, "Studio")),
                            synopsis = "Syn",
                            genres = listOf(CurrentSeasonAnimeResponse.Genre(3, "Action")),
                            broadcast = CurrentSeasonAnimeResponse.Broadcast("monday", "20:00")
                        )
                    )
                ),
                paging = CurrentSeasonAnimeResponse.Paging(next = "next"),
                season = CurrentSeasonAnimeResponse.Season(year = 2025, season = "fall")
            )
        )

        val node = domain.data.single().node
        assertEquals(1, node.id)
        assertEquals("Title", node.title)
        assertEquals("m", node.mainPicture.medium)
        assertEquals("l", node.mainPicture.large)
        assertEquals(7.5, node.mean, 0.0)
        assertEquals("currently_airing", node.status)
        assertEquals("tv", node.mediaType)
        assertEquals(12, node.numEpisodes)
        assertEquals("Studio", node.studios.single().name)
        assertEquals(2, node.studios.single().id)
        assertEquals("Syn", node.synopsis)
        assertEquals("Action", node.genres.single().name)
        assertEquals("monday", node.broadcast.dayOfTheWeek)
        assertEquals("20:00", node.broadcast.startTime)
        assertEquals("next", domain.paging.next)
        assertEquals(2025, domain.season.year)
        assertEquals("fall", domain.season.season)
    }
}

class TopAiringDomainMapperTest {

    private val mapper = TopAiringDomainMapper()

    @Test
    fun `empty response maps to defaults`() {
        val domain = mapper.mapToTopAiringAnime(TopAiringAnimeResponse())

        assertTrue(domain.data.isEmpty())
        assertEquals("", domain.paging.next)
        assertEquals(0, domain.season.year)
    }

    @Test
    fun `maps a full response`() {
        val domain = mapper.mapToTopAiringAnime(
            TopAiringAnimeResponse(
                data = listOf(
                    TopAiringAnimeResponse.Data(
                        TopAiringAnimeResponse.Node(
                            id = 4,
                            title = "Title",
                            mainPicture = TopAiringAnimeResponse.MainPicture("m", "l"),
                            mean = 8.0
                        )
                    )
                ),
                paging = TopAiringAnimeResponse.Paging("next"),
                season = TopAiringAnimeResponse.Season(2025, "fall")
            )
        )

        val node = domain.data.single().node
        assertEquals(4, node.id)
        assertEquals("Title", node.title)
        assertEquals("l", node.mainPicture.large)
        assertEquals(8.0, node.mean, 0.0)
        assertEquals("next", domain.paging.next)
        assertEquals(2025, domain.season.year)
        assertEquals("fall", domain.season.season)
    }
}

class TopRankingDomainMapperTest {

    private val mapper = TopRankingDomainMapper()

    @Test
    fun `empty response maps to defaults`() {
        val domain = mapper.mapToTopRankingAnime(TopRankingResponse())

        assertTrue(domain.data.isEmpty())
        assertEquals("", domain.paging.next)
    }

    @Test
    fun `maps a full response`() {
        val domain = mapper.mapToTopRankingAnime(
            TopRankingResponse(
                data = listOf(
                    TopRankingResponse.Data(
                        node = TopRankingResponse.Node(
                            id = 6,
                            title = "Title",
                            mainPicture = TopRankingResponse.MainPicture("m", "l"),
                            mean = 9.0,
                            broadcast = TopRankingResponse.Broadcast("sunday", "10:00"),
                            status = "finished_airing",
                            numEpisodes = 64,
                            studios = listOf(TopRankingResponse.Studio(1, "Bones")),
                            genres = listOf(TopRankingResponse.Genre(2, "Action"))
                        )
                    )
                ),
                paging = TopRankingResponse.Paging("next")
            )
        )

        val node = domain.data.single().node
        assertEquals(6, node.id)
        assertEquals("Title", node.title)
        assertEquals("l", node.mainPicture.large)
        assertEquals(9.0, node.mean, 0.0)
        assertEquals("sunday", node.broadcast.dayOfTheWeek)
        assertEquals("finished_airing", node.status)
        assertEquals(64, node.numEpisodes)
        assertEquals("Bones", node.studios.single().name)
        assertEquals("Action", node.genres.single().name)
        assertEquals("next", domain.paging.next)
    }

    @Test
    fun `null node maps to default node`() {
        val domain = mapper.mapToTopRankingAnime(
            TopRankingResponse(data = listOf(TopRankingResponse.Data(node = null)))
        )

        assertEquals("", domain.data.single().node.title)
    }
}
