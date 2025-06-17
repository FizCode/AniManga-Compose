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
 * Converts a suspendable domain-layer operation into a reactive [StateFlow] of [UiState],
 * automatically mapping the domain model to a UI model and handling common network exceptions.
 *
 * This is useful for exposing UI-friendly state to Compose or other reactive layers
 * while preserving structured error handling and transformation logic.
 *
 * ### Behavior:
 * - Invokes the provided [domain] function and maps its successful result using [mapper].
 * - Emits appropriate error states if a known exception occurs.
 * - Uses [Dispatchers.IO] for the upstream flow and scopes the result with [stateIn].
 * - Starts collecting only when subscribed, and stops 5 seconds after the last subscriber disconnects.
 *
 * @param domain A suspend function returning a [UiState] of the domain model.
 * @param mapper A transformation function converting the domain model to the UI model.
 * @param scope The [CoroutineScope] in which the resulting [StateFlow] will be active.
 *
 * @return A [StateFlow] that emits [UiState] of the UI model, starting with [UiState.Loading].
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
}.flowOn(Dispatchers.IO)
    .stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
