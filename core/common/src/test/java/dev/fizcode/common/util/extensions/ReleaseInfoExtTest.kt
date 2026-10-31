package dev.fizcode.common.util.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseInfoExtTest {

    @Test
    fun `finished titles show the episode count`() {
        assertEquals("12 Episodes | Finished", releaseInfo("", "", "finished_airing", 12))
        assertEquals("1 Episode | Finished", releaseInfo("", "", "finished_airing", 1))
        assertEquals("Finished", releaseInfo("", "", "finished_airing", 0))
    }

    @Test
    fun `airing titles show the broadcast day and time`() {
        assertEquals(
            "Friday, 23:00 (JST)",
            releaseInfo("friday", "23:00", "currently_airing", 28)
        )
        assertEquals("Unknown Airing Time", releaseInfo("", "", "currently_airing", 28))
    }

    @Test
    fun `titles that did not start show their status`() {
        assertEquals("Not Yet Aired", releaseInfo("", "", "not_yet_aired", 0))
    }

    @Test
    fun `an unknown status is reported as unknown`() {
        assertEquals("Unknown Airing Status", releaseInfo("", "", "abc", 3))
    }
}
