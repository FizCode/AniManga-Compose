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
