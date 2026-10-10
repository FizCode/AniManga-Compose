package dev.fizcode.common.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class DefaultSeasonHelper : SeasonHelper {

    /** Read on every call so the season stays correct in a long-lived process. */
    @OptIn(ExperimentalTime::class)
    private val localDate
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    override fun getCurrentSeason(): String = when (localDate.month.number) {
        12, 1, 2 -> RankingTypeConstant.WINTER
        3, 4, 5 -> RankingTypeConstant.SPRING
        6, 7, 8 -> RankingTypeConstant.SUMMER
        else -> RankingTypeConstant.FALL
    }

    override fun getCurrentYearForSeason(): Int = localDate.run {
        if (month.number == 1 || month.number == 2) year - 1 else year
    }

    override fun getCurrentYear(): Int = localDate.year
}
