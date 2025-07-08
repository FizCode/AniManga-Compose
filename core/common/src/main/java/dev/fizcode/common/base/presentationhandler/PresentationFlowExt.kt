package dev.fizcode.common.base.presentationhandler

import dev.fizcode.common.base.responsehandler.UiState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

/**
 * Converts a suspendable domain-layer operation into a reactive [StateFlow] of [UiState],
 * automatically mapping the domain model to a UI model and handling common network exceptions.
 *
 * This is useful for exposing UI-friendly state to Compose or other reactive layers
 * while preserving structured error handling and transformation logic.
 *
 * ### Behavior:
 * - Starts by emitting [UiState.Loading] whenever a trigger is received.
 * - Invokes the provided [domain] function and maps its successful result using [mapper].
 * - Emits [UiState.Success], [UiState.Empty], or one of the [UiState.Error*] variants depending on
 * the outcome.
 * - Upstream flow runs on [Dispatchers.IO].
 * - The result is shared using [stateIn] with lifecycle control via [started].
 *
 * @param scope The [CoroutineScope] in which the resulting [StateFlow] will be active.
 * @param domain A suspend function returning a [UiState] of the domain model.
 * @param mapper A transformation function converting the domain model to the UI model.
 * @param retrigger Optional [Flow] to manually trigger re-fetching, e.g. for pull-to-refresh.
 * Default: [emptyFlow].
 * @param started Defines when the upstream flow is started and stopped.
 * Default: [SharingStarted.WhileSubscribed] with 5-second timeout.
 *
 * @return A [StateFlow] that emits [UiState] of the UI model, starting with [UiState.Empty].
 */
inline fun <Domain, UiModel> asStateFlow(
    scope: CoroutineScope,
    crossinline domain: suspend () -> UiState<Domain>,
    crossinline mapper: (Domain) -> UiModel,
    retrigger: Flow<Unit> = emptyFlow(),
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
): StateFlow<UiState<UiModel>> = retrigger
    .onStart { emit(Unit) }
    .flatMapLatest {
        flow {
            emit(UiState.Loading)
            try {
                when (val domainData = domain()) {
                    is UiState.Success -> {
                        emit(UiState.Success(mapper(domainData.data)))
                    }

                    else -> emit(UiState.Empty)
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
    }.stateIn(
        scope = scope,
        started = started,
        initialValue = UiState.Empty
    )
