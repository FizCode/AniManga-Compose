package dev.fizcode.common.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NsfwFilterTest {

    @Test
    fun `safe only allows safe media`() {
        assertTrue(NsfwFilter.SAFE.allows("white"))
        assertFalse(NsfwFilter.SAFE.allows("gray"))
        assertFalse(NsfwFilter.SAFE.allows("black"))
    }

    @Test
    fun `grey also allows suggestive media`() {
        assertTrue(NsfwFilter.GREY.allows("white"))
        assertTrue(NsfwFilter.GREY.allows("gray"))
        assertFalse(NsfwFilter.GREY.allows("black"))
    }

    @Test
    fun `not safe allows everything`() {
        listOf("white", "gray", "black").forEach { assertTrue(NsfwFilter.NOT_SAFE.allows(it)) }
    }

    @Test
    fun `a media without a rating is safe`() {
        NsfwFilter.entries.forEach {
            assertTrue(it.allows(null))
            assertTrue(it.allows(""))
        }
    }

    @Test
    fun `only safe leaves nsfw media out of the request`() {
        assertEquals(
            listOf(false, true, true),
            NsfwFilter.entries.map { it.includesNsfw }
        )
    }
}
