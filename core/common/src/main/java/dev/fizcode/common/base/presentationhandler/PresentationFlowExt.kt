package dev.fizcode.common.base.presentationhandler

import dev.fizcode.common.base.responsehandler.UiState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

/**
 * Change Use Cases or Repositories logics to be [StateFlow] output.
 *
 * [flowOn] : Dispatchers.IO
 *
 * [stateIn] : started [SharingStarted.WhileSubscribed] 5.000 ms, initial value [UiState.Loading]
 *
 * @param domain Suspended function. Must be wrapped with [UiState]
 * @param mapper Mapping the Domain model to be UiModel
 * @param scope Should be on [CoroutineScope], for example: viewModelScope
 */
inline fun <Domain, UiModel> asStateFlow(
    crossinline domain: suspend () -> UiState<Domain>,
    crossinline mapper: (Domain) -> UiModel,
    scope: CoroutineScope
): StateFlow<UiState<UiModel>> = flow {
    try {
        when (val domainData = domain()) {
            is UiState.Success -> {
                emit(UiState.Success(mapper(domainData.data)))
            }

            else -> UiState.Empty
        }
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
    .flowOn(Dispatchers.IO)
    .stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
