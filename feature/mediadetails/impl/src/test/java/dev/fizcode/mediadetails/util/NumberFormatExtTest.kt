package dev.fizcode.mediadetails.util

import org.junit.Assert.assertEquals
import org.junit.Test

class NumberFormatExtTest {

    @Test
    fun `comma separators group thousands and can show zero as not available`() {
        assertEquals("999", 999.toCommaSeparators())
        assertEquals("1,234,567", 1_234_567.toCommaSeparators())
        assertEquals("N/A", 0.toCommaSeparators(zeroIsNa = true))
        assertEquals("0", 0.toCommaSeparators())
    }

    @Test
    fun `compact number switches to K and M`() {
        assertEquals("9,999", 9_999.toCompactNumber())
        assertEquals("12K", 12_345.toCompactNumber())
        assertEquals("1.2M", 1_234_567.toCompactNumber())
        assertEquals("1M", 1_000_000.toCompactNumber())
        assertEquals("N/A", 0.toCompactNumber(zeroIsNa = true))
    }

    @Test
    fun `zero is shown as not available`() {
        assertEquals("N/A", 0.toStringOrNa())
        assertEquals("N/A", 0.0.toStringOrNa())
        assertEquals("8.5", 8.5.toStringOrNa())
    }
}
