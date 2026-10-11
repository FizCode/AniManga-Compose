package dev.fizcode.settings.util

import java.util.Calendar
import org.junit.Assert.assertEquals
import org.junit.Test

class ConstantTest {

    @Test
    fun `copyright shows the given year`() {
        assertEquals("Copyright© 2027 FizCode. All Rights Reserved.", Constant.copyright(2027))
    }

    @Test
    fun `copyright defaults to the current year`() {
        val year = Calendar.getInstance().get(Calendar.YEAR)

        assertEquals("Copyright© $year FizCode. All Rights Reserved.", Constant.copyright())
    }
}
