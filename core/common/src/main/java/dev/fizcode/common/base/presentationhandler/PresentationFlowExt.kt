package dev.fizcode.common.base.presentationhandler

import dev.fizcode.common.base.callhandler.DomainLocalState
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
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
 * Converts a suspendable domain-layer function into a reactive [StateFlow] of [UiState],
 * handling loading, empty, success, and structured error states in a unified way.
 *
 * This utility is designed to bridge domain-layer operations (which return [DomainNetworkState])
 * into UI-friendly state models ([UiState]) for use in Compose or other reactive UI layers.
 *
 * ### Behavior:
 * - Emits [UiState.Loading] every time [retrigger] emits a value (defaults to a single emission on start).
 * - Executes the provided [domain] suspend function.
 * - If [domain] throws, it is caught and wrapped as [DomainNetworkState.ErrorNetwork].
 * - The result of [domain] is mapped to a [UiState] using the provided [mapper].
 * - Emits:
 *   - [UiState.Success] with the mapped result if successful,
 *   - [UiState.Empty] if the domain result was empty,
 *   - [UiState.Error*] variants depending on the exception type.
 * - Flow runs on [Dispatchers.IO].
 * - The resulting [StateFlow] is scoped to [scope] and controlled via [started].
 *
 * @param scope The [CoroutineScope] where the resulting [StateFlow] will be active.
 * @param domain A suspend function that returns a [DomainNetworkState] from the domain layer.
 * @param mapper A [noinline] function that maps a successful domain model to a UI model.
 * @param retrigger Optional [Flow] to trigger re-execution, e.g. for pull-to-refresh or retry.
 * Default: a single trigger via [emptyFlow].
 * @param started Defines when the upstream flow is started and stopped (default: 5s timeout).
 *
 * @return A cold [StateFlow] emitting [UiState] values, starting from [UiState.Empty].
 */
inline fun <Domain, UiModel> asStateFlow(
    scope: CoroutineScope,
    crossinline domain: suspend () -> DomainNetworkState<Domain>,
    noinline mapper: (Domain) -> UiModel,
    retrigger: Flow<Unit> = emptyFlow(),
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
): StateFlow<UiState<UiModel>> = retrigger
    .onStart { emit(Unit) }
    .flatMapLatest {
        flow {
            emit(UiState.Loading)

            val result = try {
                domain()
            } catch (e: Exception) {
                DomainNetworkState.ErrorNetwork(exception = e, message = e.message)
            }

            emit(result.toUiState(mapper))
        }.flowOn(Dispatchers.IO)
    }.stateIn(
        scope = scope,
        started = started,
        initialValue = UiState.Empty
    )

/**
 * Maps a [DomainNetworkState] result to a corresponding [UiState], transforming successful
 * data using the provided [mapper] and preserving structured error information.
 *
 * This is typically used to bridge domain-layer results into UI-friendly states for display.
 *
 * ### Behavior:
 * - [DomainNetworkState.Success] → mapped to [UiState.Success] using [mapper].
 * - [DomainNetworkState.Empty] → mapped to [UiState.Empty].
 * - [DomainNetworkState.ErrorNetwork] → mapped to one of the error variants in [UiState],
 *   based on the actual exception type:
 *     - [RedirectResponseException] → [UiState.ErrorRedirectResponse]
 *     - [ClientRequestException] → [UiState.ErrorClientRequest]
 *     - [ServerResponseException] → [UiState.ErrorServerResponse]
 *     - Other or null → [UiState.ErrorException]
 *
 * @param mapper Function to transform the domain model [T] into a UI model [R].
 * @return A [UiState] representing loading success, emptiness, or categorized error.
 */
fun <T, R> DomainNetworkState<T>.toUiState(mapper: (T) -> R): UiState<R> = when (this) {
    is DomainNetworkState.Success -> UiState.Success(mapper(data))
    is DomainNetworkState.Empty -> UiState.Empty
    is DomainNetworkState.ErrorNetwork -> {
        when (val e = exception) {
            is RedirectResponseException -> UiState.ErrorRedirectResponse(e, message)
            is ClientRequestException -> UiState.ErrorClientRequest(e, message)
            is ServerResponseException -> UiState.ErrorServerResponse(e, message)
            else -> UiState.ErrorException(e, message)
        }
    }
}

/**
 * Converts a suspendable database-layer function that returns a [Flow] of [DomainLocalState]
 * into a [StateFlow] of [UiState] for reactive UI consumption.
 *
 * This function observes local Room data, maps the domain model into a UI model using [mapper],
 * and handles empty or error states in a UI-friendly way.
 *
 * ### Behavior:
 * - Triggers [domain] when [retrigger] emits (default: once on start).
 * - Emits:
 *   - [UiState.Loading] on collection start.
 *   - [UiState.Success] with mapped data if [DomainLocalState.Success].
 *   - [UiState.Empty] if [DomainLocalState.Empty].
 *   - [UiState.ErrorException] if [DomainLocalState.Error].
 * - Runs on [Dispatchers.IO].
 * - Shares the result using [stateIn] with lifecycle control via [started].
 *
 * @param scope The [CoroutineScope] in which the resulting [StateFlow] will be active.
 * @param domain A suspend function returning a [Flow] of [DomainLocalState] from local DB.
 * @param mapper A transformation function to convert domain model to UI model.
 * @param retrigger Optional [Flow] to trigger collection manually (e.g., pull-to-refresh).
 * @param started Defines when upstream flow is started/stopped.
 *
 * @return A [StateFlow] emitting [UiState] based on the database state.
 */
inline fun <Domain, UiModel> asStateFlowDbCall(
    scope: CoroutineScope,
    crossinline domain: suspend () -> Flow<DomainLocalState<Domain>>,
    crossinline mapper: (Domain) -> UiModel,
    retrigger: Flow<Unit> = emptyFlow(),
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
): StateFlow<UiState<UiModel>> = retrigger
    .onStart { emit(Unit) }
    .flatMapLatest {
        flow {
            emit(UiState.Loading)

            domain().collect { state ->
                when (state) {
                    is DomainLocalState.Success -> emit(UiState.Success(mapper(state.data)))
                    is DomainLocalState.Empty -> emit(UiState.Empty)
                    is DomainLocalState.Error -> emit(
                        UiState.ErrorException(state.exception, state.message)
                    )
                }
            }
        }.flowOn(Dispatchers.IO)
    }.stateIn(
        scope = scope,
        started = started,
        initialValue = UiState.Empty
    )

