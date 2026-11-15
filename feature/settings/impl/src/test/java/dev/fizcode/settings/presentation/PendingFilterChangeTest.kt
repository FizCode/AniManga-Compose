package dev.fizcode.settings.presentation

import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.TitleStyle
import dev.fizcode.settings.presentation.model.PendingFilterChange
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PendingFilterChangeTest {

    private val notSafe = PendingFilterChange.Nsfw(NsfwFilter.NOT_SAFE)

    @Test
    fun `not safe asks for the age phrase`() {
        assertEquals("I'm above 21", notSafe.confirmationPhrase)
    }

    @Test
    fun `grey and titling need no phrase`() {
        val grey = PendingFilterChange.Nsfw(NsfwFilter.GREY)
        val titling = PendingFilterChange.Titling(TitleStyle.ENGLISH)

        assertNull(grey.confirmationPhrase)
        assertNull(titling.confirmationPhrase)
        assertTrue(grey.isConfirmed(""))
        assertTrue(titling.isConfirmed(""))
    }

    @Test
    fun `not safe is confirmed only by the phrase`() {
        assertFalse(notSafe.isConfirmed(""))
        assertFalse(notSafe.isConfirmed("I'm above 20"))
        assertTrue(notSafe.isConfirmed("I'm above 21"))
    }

    @Test
    fun `the phrase ignores case, outer spaces and the curly apostrophe`() {
        assertTrue(notSafe.isConfirmed("  i'M ABOVE 21 "))
        assertTrue(notSafe.isConfirmed("I’m above 21"))
    }
}
