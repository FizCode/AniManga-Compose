package dev.fizcode.mediadetails.presentation

import dev.fizcode.common.util.DEEPLINK_BASE
import org.junit.Assert.assertEquals
import org.junit.Test

class ShareTextTest {

    @Test
    fun `deep link follows the details path`() {
        assertEquals("$DEEPLINK_BASE/details/tv/1", mediaDetailsDeepLink("tv", 1))
    }

    @Test
    fun `media type with spaces is percent encoded`() {
        assertEquals(
            "$DEEPLINK_BASE/details/Light%20Novel/7",
            mediaDetailsDeepLink("Light Novel", 7)
        )
    }

    @Test
    fun `share text has title content and link`() {
        assertEquals(
            "Bleach\n\nA synopsis.\n\n$DEEPLINK_BASE/details/tv/269",
            buildShareText(title = " Bleach ", content = " A synopsis. ", mediaType = "tv", mediaId = 269)
        )
    }

    @Test
    fun `blank parts are left out`() {
        assertEquals(
            "$DEEPLINK_BASE/details/manga/2",
            buildShareText(title = "", content = "  ", mediaType = "manga", mediaId = 2)
        )
    }
}
