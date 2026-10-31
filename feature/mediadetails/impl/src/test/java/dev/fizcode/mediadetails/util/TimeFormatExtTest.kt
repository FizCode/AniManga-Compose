package dev.fizcode.mediadetails.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatExtTest {

    @Test
    fun `only the non zero units are shown`() {
        assertEquals("25 mins.", 1500.toFormattedTime())
        assertEquals("1 hour", 3600.toFormattedTime())
        assertEquals("1 hour 1 min. 1 sec.", 3661.toFormattedTime())
    }

    @Test
    fun `zero seconds is still shown`() {
        assertEquals("0 secs.", 0.toFormattedTime())
    }
}
