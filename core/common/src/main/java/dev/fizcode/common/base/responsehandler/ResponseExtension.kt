package dev.fizcode.common.base.responsehandler

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

inline fun <Response> processResponse(response: () -> Response): UiState<Response> {
    return try {
        UiState.Success(data = response())
    } catch (e: Exception) {
        UiState.ErrorException(e)
    }
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
