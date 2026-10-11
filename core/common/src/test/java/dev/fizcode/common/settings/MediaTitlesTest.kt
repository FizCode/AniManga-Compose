package dev.fizcode.common.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaTitlesTest {

    private val titles = MediaTitles(
        default = "Sen to Chihiro no Kamikakushi",
        alternative = AlternativeTitles(
            synonyms = listOf("Sen to Chihiro"),
            japanese = "千と千尋の神隠し",
            english = "Spirited Away"
        )
    )

    @Test
    fun `resolves the title of each style`() {
        assertEquals("Sen to Chihiro no Kamikakushi", titles.resolve(TitleStyle.DEFAULT))
        assertEquals("Sen to Chihiro", titles.resolve(TitleStyle.SYNONYM))
        assertEquals("千と千尋の神隠し", titles.resolve(TitleStyle.JAPANESE))
        assertEquals("Spirited Away", titles.resolve(TitleStyle.ENGLISH))
    }

    @Test
    fun `keeps the default title when the media has no alternative titles`() {
        val plain = MediaTitles("Bleach")

        TitleStyle.entries.forEach { assertEquals("Bleach", plain.resolve(it)) }
    }

    @Test
    fun `matches any of the titles`() {
        assertTrue(titles.matches("spirited"))
        assertTrue(titles.matches("Kamikakushi"))
        assertTrue(titles.matches("千尋"))
        assertTrue(titles.matches(""))
        assertFalse(titles.matches("Naruto"))
    }
}
