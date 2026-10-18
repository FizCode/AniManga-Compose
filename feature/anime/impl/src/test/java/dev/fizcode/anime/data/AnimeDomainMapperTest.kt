package dev.fizcode.anime.data

import dev.fizcode.anime.data.mapper.SeasonalDomainMapper
import dev.fizcode.anime.data.mapper.TopAiringDomainMapper
import dev.fizcode.anime.data.mapper.TopRankingDomainMapper
import dev.fizcode.mal.response.MalAnimeNode
import dev.fizcode.mal.response.MalBroadcast
import dev.fizcode.mal.response.MalGenre
import dev.fizcode.mal.response.MalListItem
import dev.fizcode.mal.response.MalListResponse
import dev.fizcode.mal.response.MalPaging
import dev.fizcode.mal.response.MalPicture
import dev.fizcode.mal.response.MalSeason
import dev.fizcode.mal.response.MalStudio
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeasonalDomainMapperTest {

    private val mapper = SeasonalDomainMapper()

    @Test
    fun `empty response maps to defaults`() {
        val domain = mapper.mapToSeasonAnime(MalListResponse())

        assertTrue(domain.data.isEmpty())
        assertEquals("", domain.paging.next)
        assertEquals(0, domain.season.year)
        assertEquals("", domain.season.season)
    }

    @Test
    fun `null node maps to default node`() {
        val domain = mapper.mapToSeasonAnime(
            MalListResponse<MalAnimeNode>(data = listOf(MalListItem(null)))
        )

        assertEquals(0, domain.data.single().node.id)
        assertEquals("", domain.data.single().node.title)
    }

    @Test
    fun `maps a full response`() {
        val domain = mapper.mapToSeasonAnime(
            MalListResponse(
                data = listOf(
                    MalListItem(
                        MalAnimeNode(
                            id = 1,
                            title = "Title",
                            mainPicture = MalPicture("m", "l"),
                            mean = 7.5,
                            status = "currently_airing",
                            mediaType = "tv",
                            numEpisodes = 12,
                            studios = listOf(MalStudio(2, "Studio")),
                            synopsis = "Syn",
                            genres = listOf(MalGenre(3, "Action")),
                            broadcast = MalBroadcast("monday", "20:00")
                        )
                    )
                ),
                paging = MalPaging(next = "next"),
                season = MalSeason(year = 2025, season = "fall")
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
        val domain = mapper.mapToTopAiringAnime(MalListResponse())

        assertTrue(domain.data.isEmpty())
        assertEquals("", domain.paging.next)
        assertEquals(0, domain.season.year)
    }

    @Test
    fun `maps a full response`() {
        val domain = mapper.mapToTopAiringAnime(
            MalListResponse(
                data = listOf(
                    MalListItem(
                        MalAnimeNode(
                            id = 4,
                            title = "Title",
                            mainPicture = MalPicture("m", "l"),
                            mean = 8.0
                        )
                    )
                ),
                paging = MalPaging("next"),
                season = MalSeason(2025, "fall")
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
        val domain = mapper.mapToTopRankingAnime(MalListResponse())

        assertTrue(domain.data.isEmpty())
        assertEquals("", domain.paging.next)
    }

    @Test
    fun `maps a full response`() {
        val domain = mapper.mapToTopRankingAnime(
            MalListResponse(
                data = listOf(
                    MalListItem(
                        node = MalAnimeNode(
                            id = 6,
                            title = "Title",
                            mainPicture = MalPicture("m", "l"),
                            mean = 9.0,
                            broadcast = MalBroadcast("sunday", "10:00"),
                            status = "finished_airing",
                            numEpisodes = 64,
                            studios = listOf(MalStudio(1, "Bones")),
                            genres = listOf(MalGenre(2, "Action"))
                        )
                    )
                ),
                paging = MalPaging("next")
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
            MalListResponse<MalAnimeNode>(data = listOf(MalListItem(node = null)))
        )

        assertEquals("", domain.data.single().node.title)
    }
}
