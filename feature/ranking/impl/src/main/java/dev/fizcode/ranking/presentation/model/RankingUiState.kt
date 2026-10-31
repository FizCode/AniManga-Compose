package dev.fizcode.ranking.presentation.model

import dev.fizcode.common.base.callhandler.UiState

internal data class RankingUiState(
    val query: String = "",
    val ranking: UiState<List<RankingUiModel>> = UiState.Loading
) {
    val result: UiState<List<RankingUiModel>> = when {
        ranking is UiState.Success && query.isNotEmpty() -> ranking.data
            .filter { it.titles.matches(query) }
            .let { if (it.isEmpty()) UiState.Empty else UiState.Success(it) }
        else -> ranking
    }
}
