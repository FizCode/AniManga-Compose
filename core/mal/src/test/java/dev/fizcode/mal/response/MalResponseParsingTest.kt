package dev.fizcode.mal.response

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Parses sample MyAnimeList answers the same way the HTTP client does. */
class MalResponseParsingTest {

    private val json = Json {
        coerceInputValues = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Test
    fun `anime ranking keeps the ranking and the extra fields`() {
        val response = json.decodeFromString<MalListResponse<MalAnimeNode>>(
            """
            {
              "data": [
                {
                  "node": {
                    "id": 5114,
                    "title": "Fullmetal Alchemist: Brotherhood",
                    "alternative_titles": {
                      "synonyms": ["Hagane no Renkinjutsushi"],
                      "en": "Fullmetal Alchemist: Brotherhood",
                      "ja": "鋼の錬金術師"
                    },
                    "nsfw": "white",
                    "main_picture": {"medium": "m.jpg", "large": "l.jpg"},
                    "mean": 9.1,
                    "status": "finished_airing",
                    "media_type": "tv",
                    "num_episodes": 64,
                    "studios": [{"id": 4, "name": "Bones"}],
                    "synopsis": "Syn",
                    "genres": [{"id": 1, "name": "Action"}],
                    "broadcast": {"day_of_the_week": "sunday", "start_time": "17:00"},
                    "unknown_field": 1
                  },
                  "ranking": {"rank": 1}
                }
              ],
              "paging": {"next": "https://api.myanimelist.net/v2/anime/ranking?offset=1"}
            }
            """.trimIndent()
        )

        val item = response.data.orEmpty().single()
        val node = item.node!!
        assertEquals(1, item.ranking?.rank)
        assertEquals(5114, node.id)
        assertEquals("white", node.nsfw)
        assertEquals(listOf("Hagane no Renkinjutsushi"), node.alternativeTitles?.synonyms)
        assertEquals("鋼の錬金術師", node.alternativeTitles?.ja)
        assertEquals("l.jpg", node.mainPicture?.large)
        assertEquals("Bones", node.studios?.single()?.name)
        assertEquals("sunday", node.broadcast?.dayOfTheWeek)
        assertEquals("17:00", node.broadcast?.startTime)
        assertTrue(response.paging?.next.orEmpty().isNotEmpty())
        assertNull(response.season)
    }

    @Test
    fun `season answer carries the season`() {
        val response = json.decodeFromString<MalListResponse<MalAnimeNode>>(
            """{"data": [], "season": {"year": 2026, "season": "fall"}}"""
        )

        assertEquals(2026, response.season?.year)
        assertEquals("fall", response.season?.season)
        assertTrue(response.data.orEmpty().isEmpty())
    }

    @Test
    fun `manga ranking parses the manga fields`() {
        val response = json.decodeFromString<MalListResponse<MalMangaNode>>(
            """
            {
              "data": [
                {
                  "node": {
                    "id": 2,
                    "title": "Berserk",
                    "nsfw": "black",
                    "num_volumes": 41,
                    "num_chapters": 364,
                    "media_type": "manga",
                    "genres": [{"id": 1, "name": "Action"}]
                  },
                  "ranking": {"rank": 1}
                }
              ]
            }
            """.trimIndent()
        )

        val node = response.data.orEmpty().single().node!!
        assertEquals("black", node.nsfw)
        assertEquals(41, node.numVolumes)
        assertEquals(364, node.numChapters)
        assertEquals("Action", node.genres?.single()?.name)
    }

    @Test
    fun `missing fields stay null and null alternative titles map to empty ones`() {
        val node = json.decodeFromString<MalListResponse<MalAnimeNode>>(
            """{"data": [{"node": {"id": 7}}]}"""
        ).data.orEmpty().single().node!!

        assertNull(node.title)
        assertNull(node.nsfw)
        val alternative = node.alternativeTitles.toAlternativeTitles()
        assertTrue(alternative.synonyms.isEmpty())
        assertEquals("", alternative.japanese)
        assertEquals("", alternative.english)
    }
}
