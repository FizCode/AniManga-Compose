package dev.fizcode.common.base.responsehandler

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

sealed class UiState<out T> {
    data object Empty: UiState<Nothing>()
    data object Loading: UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    // 3xx - responses
    data class ErrorRedirectResponse(
        val error: RedirectResponseException,
        val message: String? = null
    ): UiState<Nothing>()
    // 4xx - responses
    data class ErrorClientRequest(
        val error: ClientRequestException,
        val message: String? = null
    ): UiState<Nothing>()
    // 5xx - responses
    data class ErrorServerResponse(
        val error: ServerResponseException,
        val message: String? = null
    ) : UiState<Nothing>()
    // Other Errors
    data class ErrorException(
        val error: Exception? = null,
        val message: String? = null
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

fun <T> UiState<T>.onErrorRedirectResponse(
    execute: (error: RedirectResponseException?, message: String?) -> Unit
): UiState<T> = apply {
    if (this is UiState.ErrorRedirectResponse) {
        execute(error, message)
    }
}

fun <T> UiState<T>.onErrorClientRequest(
    execute: (error: ClientRequestException?, message: String?) -> Unit
): UiState<T> = apply {
    if (this is UiState.ErrorClientRequest) {
        execute(error, message)
    }
}

fun <T> UiState<T>.onErrorServerResponse(
    execute: (error: ServerResponseException?, message: String?) -> Unit
): UiState<T> = apply {
    if (this is UiState.ErrorServerResponse) {
        execute(error, message)
    }
}

fun <T> UiState<T>.onErrorException(
    execute: (error: Exception?, message: String?) -> Unit
): UiState<T> = apply {
    if (this is UiState.ErrorException) {
        execute(error, message)
    }
}
