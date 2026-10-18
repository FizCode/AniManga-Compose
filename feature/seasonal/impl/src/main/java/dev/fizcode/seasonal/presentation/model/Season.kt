package dev.fizcode.seasonal.presentation.model

import dev.fizcode.common.util.RankingTypeConstant

/** A MAL anime season: [apiValue] is the path segment sent to the API, [label] is shown on the tab. */
internal enum class Season(val apiValue: String, val label: String) {
    WINTER(RankingTypeConstant.WINTER, "Winter"),
    SPRING(RankingTypeConstant.SPRING, "Spring"),
    SUMMER(RankingTypeConstant.SUMMER, "Summer"),
    FALL(RankingTypeConstant.FALL, "Fall");

    companion object {
        fun fromApiValue(value: String): Season = entries.firstOrNull { it.apiValue == value } ?: WINTER
    }
}
