package dev.fizcode.common.settings

import org.junit.Assert.assertEquals
import org.junit.Test

class TitleStyleTest {

    private fun TitleStyle.pick(
        synonyms: List<String> = listOf("Sen to Chihiro"),
        japanese: String? = "千と千尋の神隠し",
        english: String? = "Spirited Away"
    ) = resolve(default = "Sen to Chihiro no Kamikakushi", synonyms, japanese, english)

    @Test
    fun `default style keeps the default title`() {
        assertEquals("Sen to Chihiro no Kamikakushi", TitleStyle.DEFAULT.pick())
    }

    @Test
    fun `synonym style uses the first synonym`() {
        assertEquals("Sen to Chihiro", TitleStyle.SYNONYM.pick())
    }

    @Test
    fun `japanese and english styles use their own titles`() {
        assertEquals("千と千尋の神隠し", TitleStyle.JAPANESE.pick())
        assertEquals("Spirited Away", TitleStyle.ENGLISH.pick())
    }

    @Test
    fun `a media without a synonym falls back to the default title`() {
        assertEquals("Sen to Chihiro no Kamikakushi", TitleStyle.SYNONYM.pick(synonyms = emptyList()))
        assertEquals(
            "Sen to Chihiro no Kamikakushi",
            TitleStyle.SYNONYM.pick(synonyms = listOf("", "  "))
        )
    }

    @Test
    fun `a missing or blank title falls back to the default title`() {
        assertEquals("Sen to Chihiro no Kamikakushi", TitleStyle.JAPANESE.pick(japanese = null))
        assertEquals("Sen to Chihiro no Kamikakushi", TitleStyle.ENGLISH.pick(english = " "))
    }
}
