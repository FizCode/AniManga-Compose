package dev.fizcode.mal.util

import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class DefaultSeasonHelperTest {

    private fun helperAt(instant: String) = DefaultSeasonHelper(
        clock = object : Clock {
            override fun now() = Instant.parse(instant)
        },
        timeZone = { TimeZone.UTC }
    )

    @Test
    fun `the season and year follow the clock`() {
        assertEquals(Season.SPRING, helperAt("2026-04-15T12:00:00Z").getCurrentSeason())
        assertEquals(Season.SUMMER, helperAt("2026-07-15T12:00:00Z").getCurrentSeason())
        assertEquals(Season.FALL, helperAt("2026-10-15T12:00:00Z").getCurrentSeason())
        assertEquals(2026, helperAt("2026-07-15T12:00:00Z").getCurrentYear())
    }

    @Test
    fun `the time zone decides the local date`() {
        val newYear = Instant.parse("2026-12-31T23:30:00Z")
        val helper = DefaultSeasonHelper(
            clock = object : Clock {
                override fun now() = newYear
            },
            timeZone = { TimeZone.of("Asia/Tokyo") }
        )

        assertEquals(2027, helper.getCurrentYear())
    }
}
