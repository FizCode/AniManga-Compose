package dev.fizcode.manga.presentation.model

import dev.fizcode.common.base.callhandler.UiState

internal data class RankedManga(
    val request: MangaRankingRequest? = null,
    val state: UiState<List<MangaUiModel>> = UiState.Loading
)
