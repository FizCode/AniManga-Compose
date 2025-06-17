package dev.fizcode.common.util

import kotlinx.datetime.toLocalDateTime

object SeasonHelper {

    private val localDate = kotlinx.datetime.Clock.System.now()
        .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())

    private val currentMonth = localDate.monthNumber
    private val currentYear  = localDate.year

    fun getCurrentSeason(): String = when (currentMonth) {
        12, 1, 2 -> RankingTypeConstant.WINTER
        3, 4, 5 -> RankingTypeConstant.SPRING
        6, 7, 8 -> RankingTypeConstant.SUMMER
        else -> RankingTypeConstant.FALL
    }

    fun getCurrentYearForSeason(): Int =
        if (currentMonth == 1 || currentMonth == 2) currentYear -1 else currentYear

    fun getCurrentYear(): Int = currentYear

}
