package dev.fizcode.common.base.callhandler

import dev.fizcode.common.util.ErrorType

sealed class UiState<out T> {
    data object Empty: UiState<Nothing>()
    data object Loading: UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * A failed load.
     *
     * @param message text describing the failure, if any.
     * @param type category of the failure, e.g. client, server or network.
     * @param code HTTP status code, if the failure came from a response.
     * @param cause the exception behind the failure, if any.
     */
    data class Error(
        val message: String? = null,
        val type: ErrorType = ErrorType.Unknown,
        val code: Int? = null,
        val cause: Exception? = null
    ) : UiState<Nothing>()
}

fun <T> UiState<T>.onLoading(
    execute: () -> Unit
): UiState<T> = apply {
    if (this is UiState.Loading) {
        execute()
    }
}

fun <T> UiState<T>.onSuccess(
    execute: (data: T) -> Unit
): UiState<T> = apply {
    if (this is UiState.Success) {
        execute(data)
    }
}

fun <T> UiState<T>.onError(
    execute: (error: UiState.Error) -> Unit
): UiState<T> = apply {
    if (this is UiState.Error) {
        execute(this)
    }
}
