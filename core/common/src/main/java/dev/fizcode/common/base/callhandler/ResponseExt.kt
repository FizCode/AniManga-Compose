package dev.fizcode.common.base.callhandler

import dev.fizcode.common.util.ErrorType
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException

/**
 * Processes a network or domain response and maps it to a standardized [DomainNetworkState].
 *
 * This function attempts to execute the given [response] lambda, and returns:
 * - [DomainNetworkState.Success] if the result is non-empty,
 * - [DomainNetworkState.Empty] if the result is considered empty (e.g., empty list, map, string, or null),
 * - [DomainNetworkState.ErrorNetwork] if an exception is thrown during execution.
 *
 * The function uses the following rules to determine if the result is "empty":
 * - `null` is considered empty
 * - `Collection` and `Map` types are considered empty if they have no elements
 * - `String` is considered empty if it is blank
 *
 * @param Response The type of the response returned by the [response] lambda.
 * @param response A lambda function that produces a value of type [Response].
 * @return A [DomainNetworkState] representing the outcome of the operation.
 */
inline fun <Response> processResponse(response: () -> Response): DomainNetworkState<Response> =
    try {
        val result = response()

        val isEmpty = when (result) {
            is Collection<*> -> result.isEmpty()
            is Map<*, *> -> result.isEmpty()
            is String -> result.isBlank()
            null -> true
            else -> false
        }

        if (isEmpty) DomainNetworkState.Empty
        else DomainNetworkState.Success(result)

    } catch (e: Exception) {
        /**
         * Maps different exception types to their corresponding error types.
         */
        val (type, code) = when (e) {
            is RedirectResponseException -> ErrorType.Redirect to e.response.status.value
            is ClientRequestException -> ErrorType.Client to e.response.status.value
            is ServerResponseException -> ErrorType.Server to e.response.status.value
            is SocketTimeoutException, is IOException -> ErrorType.Network to null
            else -> ErrorType.Unknown to null
        }

        DomainNetworkState.ErrorNetwork(
            exception = e,
            message = e.message,
            code = code,
            type = type
        )
    }
