package dev.fizcode.common.base.responsehandler

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

/**
 * Executes a given network or API call and returns the result wrapped in a [UiState].
 *
 * This inline function is designed to execute a suspending or blocking Ktor API call
 * and catch various HTTP-related exceptions thrown by the Ktor client. Based on the type
 * of exception, it maps the result to a corresponding [UiState] subclass.
 *
 * @param Response The type of the expected response data.
 * @param response A lambda function representing the API call or network request.
 * @return A [UiState] representing either a successful result or an error state
 *         based on the type of exception thrown during execution.
 *
 * ## Behavior by HTTP Status Code:
 * - **2xx (Success)**: Returns [UiState.Success] containing the result.
 * - **3xx (Redirection)**: Returns [UiState.ErrorRedirectResponse] if a [RedirectResponseException] is thrown.
 * - **4xx (Client Error)**: Returns [UiState.ErrorClientRequest] if a [ClientRequestException] is thrown.
 * - **5xx (Server Error)**: Returns [UiState.ErrorServerResponse] if a [ServerResponseException] is thrown.
 * - **Other/Unknown Errors**: Returns [UiState.ErrorException] for all other exceptions.
 */
inline fun <Response> processResponse(response: () -> Response): UiState<Response> {
    return try {
        // 2xx - responses
        UiState.Success(data = response())
    } catch (e: RedirectResponseException) {
        // 3xx - responses
        UiState.ErrorRedirectResponse(e)
    } catch (e: ClientRequestException) {
        // 4xx - responses
        UiState.ErrorClientRequest(e)
    } catch (e: ServerResponseException) {
        // 5xx - responses
        UiState.ErrorServerResponse(e)
    } catch (e: Exception) {
        UiState.ErrorException(e)
    }
}
