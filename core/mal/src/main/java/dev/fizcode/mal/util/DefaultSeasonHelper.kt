package dev.fizcode.mal.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DefaultSeasonHelper(
    private val clock: Clock = Clock.System,
    private val timeZone: () -> TimeZone = TimeZone::currentSystemDefault
) : SeasonHelper {

    private val localDate
        get() = clock.now().toLocalDateTime(timeZone())

    override fun getCurrentSeason(): Season = when (localDate.month.number) {
        12, 1, 2 -> Season.WINTER
        3, 4, 5 -> Season.SPRING
        6, 7, 8 -> Season.SUMMER
        else -> Season.FALL
    }

    override fun getCurrentYearForSeason(): Int = localDate.run {
        if (month.number == 1 || month.number == 2) year - 1 else year
    }

    override fun getCurrentYear(): Int = localDate.year
}
