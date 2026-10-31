package dev.fizcode.seasonal.presentation.model

import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.mal.util.Season
import kotlinx.collections.immutable.ImmutableList

internal data class SeasonalUiState(
    val season: Season,
    val year: Int,
    val years: ImmutableList<Int>,
    val query: String = "",
    val seasonal: UiState<List<SeasonalUiModel>> = UiState.Loading
) {
    val result: UiState<List<SeasonalUiModel>> = when {
        seasonal is UiState.Success && query.isNotEmpty() -> seasonal.data
            .filter { it.titles.matches(query) }
            .let { if (it.isEmpty()) UiState.Empty else UiState.Success(it) }
        else -> seasonal
    }
}
