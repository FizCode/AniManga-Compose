package dev.fizcode.search.presentation.model

import dev.fizcode.common.base.callhandler.UiState

internal data class SearchUiState(
    val query: String = "",
    val result: UiState<List<SearchResultUiModel>> = UiState.Empty
)
