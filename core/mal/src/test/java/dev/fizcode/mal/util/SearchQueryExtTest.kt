package dev.fizcode.mal.util

import org.junit.Assert.assertEquals
import org.junit.Test

class SearchQueryExtTest {

    @Test
    fun `pads a query shorter than three characters`() {
        assertEquals("k__", malSearchQuery("k"))
        assertEquals("ao_", malSearchQuery("ao"))
    }

    @Test
    fun `leaves a query of three characters or more as is`() {
        assertEquals("kab", malSearchQuery("kab"))
        assertEquals("bleach", malSearchQuery("bleach"))
    }
}
