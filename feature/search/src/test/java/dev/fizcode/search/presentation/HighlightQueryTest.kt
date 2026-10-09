package dev.fizcode.search.presentation

import androidx.compose.ui.graphics.Color
import dev.fizcode.search.presentation.util.highlightQuery
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HighlightQueryTest {

    private val color = Color.Red

    @Test
    fun `blank query leaves text untouched`() {
        val result = highlightQuery("Bleach", "  ", color)

        assertEquals("Bleach", result.text)
        assertTrue(result.spanStyles.isEmpty())
    }

    @Test
    fun `match is case insensitive and keeps original casing`() {
        val result = highlightQuery("Bleach", "BLE", color)

        assertEquals("Bleach", result.text)
        val span = result.spanStyles.single()
        assertEquals(0, span.start)
        assertEquals(3, span.end)
        assertEquals(color, span.item.color)
    }

    @Test
    fun `every occurrence is highlighted`() {
        val result = highlightQuery("ababab", "ab", color)

        assertEquals(listOf(0 to 2, 2 to 4, 4 to 6), result.spanStyles.map { it.start to it.end })
    }

    @Test
    fun `no match returns plain text`() {
        val result = highlightQuery("Naruto", "xyz", color)

        assertEquals("Naruto", result.text)
        assertTrue(result.spanStyles.isEmpty())
    }
}
