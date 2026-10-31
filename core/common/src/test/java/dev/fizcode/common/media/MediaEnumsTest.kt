package dev.fizcode.common.media

import org.junit.Assert.assertEquals
import org.junit.Test

class MediaEnumsTest {

    @Test
    fun `airing status is read from the api value`() {
        assertEquals(AiringStatus.FINISHED, AiringStatus.fromApi("finished_airing"))
        assertEquals(AiringStatus.CURRENTLY_AIRING, AiringStatus.fromApi("currently_airing"))
        assertEquals(AiringStatus.NOT_YET_AIRED, AiringStatus.fromApi("not_yet_aired"))
    }

    @Test
    fun `an unknown airing status falls back to unknown`() {
        assertEquals(AiringStatus.UNKNOWN, AiringStatus.fromApi("abc"))
        assertEquals(AiringStatus.UNKNOWN, AiringStatus.fromApi(""))
    }

    @Test
    fun `media type is read from the api value`() {
        assertEquals("TV", AnimeMediaType.fromApi("tv").label)
        assertEquals("TV Special", AnimeMediaType.fromApi("tv_special").label)
        assertEquals("ONA", AnimeMediaType.fromApi("ona").label)
    }

    @Test
    fun `an unknown media type falls back to unknown`() {
        assertEquals(AnimeMediaType.UNKNOWN, AnimeMediaType.fromApi("-"))
        assertEquals("Unknown Media Type", AnimeMediaType.fromApi("-").label)
    }

    @Test
    fun `every api value is read back to the same entry`() {
        AiringStatus.entries.filter { it != AiringStatus.UNKNOWN }
            .forEach { assertEquals(it, AiringStatus.fromApi(it.apiValue)) }
        AnimeMediaType.entries.filter { it != AnimeMediaType.UNKNOWN }
            .forEach { assertEquals(it, AnimeMediaType.fromApi(it.apiValue)) }
    }
}
