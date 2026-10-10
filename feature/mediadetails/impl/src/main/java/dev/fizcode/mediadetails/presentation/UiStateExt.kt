package dev.fizcode.mediadetails.presentation

import dev.fizcode.common.base.callhandler.UiState

internal fun UiState<*>.isError(): Boolean = this is UiState.ErrorRedirectResponse ||
        this is UiState.ErrorClientRequest ||
        this is UiState.ErrorServerResponse ||
        this is UiState.ErrorException
