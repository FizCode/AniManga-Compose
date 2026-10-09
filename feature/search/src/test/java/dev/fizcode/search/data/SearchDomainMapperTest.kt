package dev.fizcode.search.data

import dev.fizcode.datasource.remote.response.SearchAnimeResponse
import dev.fizcode.search.data.mapper.SearchDomainMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchDomainMapperTest {

    private val mapper = SearchDomainMapper()

    @Test
    fun `null data maps to empty list`() {
        assertTrue(mapper.mapToSearchAnime(SearchAnimeResponse()).data.isEmpty())
    }

    @Test
    fun `entries without a node are dropped`() {
        val response = SearchAnimeResponse(
            data = listOf(
                SearchAnimeResponse.Data(node = null),
                SearchAnimeResponse.Data(node = SearchAnimeResponse.Node(id = 5))
            )
        )

        assertEquals(listOf(5), mapper.mapToSearchAnime(response).data.map { it.id })
    }

    @Test
    fun `null fields fall back to defaults`() {
        val node = mapper.mapToSearchAnime(
            SearchAnimeResponse(data = listOf(SearchAnimeResponse.Data(SearchAnimeResponse.Node())))
        ).data.single()

        assertEquals(0, node.id)
        assertEquals("", node.title)
        assertEquals("", node.posterPath)
        assertEquals(0.0, node.mean, 0.0)
        assertEquals(0, node.numEpisodes)
        assertTrue(node.genres.isEmpty())
    }

    @Test
    fun `maps all fields and uses large picture`() {
        val node = mapper.mapToSearchAnime(
            SearchAnimeResponse(
                data = listOf(
                    SearchAnimeResponse.Data(
                        SearchAnimeResponse.Node(
                            id = 9,
                            title = "Bleach",
                            mainPicture = SearchAnimeResponse.MainPicture(medium = "m", large = "l"),
                            mean = 8.5,
                            status = "finished_airing",
                            mediaType = "tv",
                            numEpisodes = 366,
                            genres = listOf(
                                SearchAnimeResponse.Genre(1, "Action"),
                                SearchAnimeResponse.Genre(2, null)
                            )
                        )
                    )
                )
            )
        ).data.single()

        assertEquals(9, node.id)
        assertEquals("Bleach", node.title)
        assertEquals("l", node.posterPath)
        assertEquals(8.5, node.mean, 0.0)
        assertEquals("finished_airing", node.status)
        assertEquals("tv", node.mediaType)
        assertEquals(366, node.numEpisodes)
        assertEquals(listOf("Action"), node.genres)
    }
}
