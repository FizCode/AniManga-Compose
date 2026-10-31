package dev.fizcode.mediadetails.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class MediaDetailsDeepLinkTest {

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
}
