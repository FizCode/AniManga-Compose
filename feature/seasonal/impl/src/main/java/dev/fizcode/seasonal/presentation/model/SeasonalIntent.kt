package dev.fizcode.seasonal.presentation.model

import dev.fizcode.common.base.mvi.UiIntent
import dev.fizcode.mal.util.Season

internal sealed interface SeasonalIntent : UiIntent {
    data class SelectSeason(val season: Season) : SeasonalIntent
    data class SelectYear(val year: Int) : SeasonalIntent
    data class QueryChanged(val query: String) : SeasonalIntent
    data object Retry : SeasonalIntent
}
