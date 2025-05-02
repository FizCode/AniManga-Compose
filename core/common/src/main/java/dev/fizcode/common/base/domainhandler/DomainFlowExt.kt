package dev.fizcode.common.base.domainhandler

import dev.fizcode.common.base.responsehandler.UiState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <Response> processData(crossinline response: () -> Response): Flow<UiState<Response>> =
    flow {
        emit(UiState.Loading)
        try {
            val data = response()
            emit(UiState.Success(data))
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            emit(UiState.ErrorRedirectResponse(error = e, message = e.message))
        } catch (e: ClientRequestException) {
            // 4xx - responses
            emit(UiState.ErrorClientRequest(error = e, message = e.message))
        } catch (e: ServerResponseException) {
            // 5xx - responses
            emit(UiState.ErrorServerResponse(error = e, message = e.message))
        } catch (e: Exception) {
            emit(UiState.ErrorException(error = e, message = e.message))
        }
    }
