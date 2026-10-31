package dev.fizcode.mediadetails.presentation.people.model

import dev.fizcode.common.base.callhandler.UiState

internal data class PagedListUiState<U>(
    val items: UiState<PagedItems<U>> = UiState.Loading
)
