package dev.fizcode.anime.presentation.model

import dev.fizcode.common.base.callhandler.UiState

internal data class AnimeUiState(
    val currentSeason: UiState<List<SeasonalUiModel>> = UiState.Loading,
    val topAiring: UiState<List<TopAiringUiModel>> = UiState.Loading,
    val topRanking: UiState<List<TopRankingUiModel>> = UiState.Loading,
    val isRefreshing: Boolean = false
) {
    val isLoading: Boolean
        get() = currentSeason is UiState.Loading ||
            topAiring is UiState.Loading ||
            topRanking is UiState.Loading

    val isAllFailed: Boolean
        get() = currentSeason is UiState.Error &&
            topAiring is UiState.Error &&
            topRanking is UiState.Error

    fun refreshing() = copy(
        currentSeason = UiState.Loading,
        topAiring = UiState.Loading,
        topRanking = UiState.Loading,
        isRefreshing = true
    )

    fun settled() = if (isRefreshing && !isLoading) copy(isRefreshing = false) else this
}
