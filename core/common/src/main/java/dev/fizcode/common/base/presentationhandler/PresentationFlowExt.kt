package dev.fizcode.common.base.presentationhandler

import dev.fizcode.common.base.callhandler.DomainLocalState
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.callhandler.catchAsNetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Maps a [DomainNetworkState] result to a corresponding [UiState], transforming successful
 * data using the provided [mapper] and preserving structured error information.
 *
 * This is typically used to bridge domain-layer results into UI-friendly states for display.
 *
 * ### Behavior:
 * - [DomainNetworkState.Success] → mapped to [UiState.Success] using [mapper].
 * - [DomainNetworkState.Empty] → mapped to [UiState.Empty].
 * - [DomainNetworkState.ErrorNetwork] → mapped to [UiState.Error], keeping its message, type,
 *   HTTP code and exception.
 *
 * @param mapper Function to transform the domain model [T] into a UI model [R].
 * @return A [UiState] representing loading success, emptiness, or categorized error.
 */
fun <T, R> DomainNetworkState<T>.toUiState(mapper: (T) -> R): UiState<R> = when (this) {
    is DomainNetworkState.Success -> UiState.Success(mapper(data))
    is DomainNetworkState.Empty -> UiState.Empty
    is DomainNetworkState.ErrorNetwork -> UiState.Error(message, type, code, exception)
}

fun <T, R> DomainLocalState<T>.toUiState(mapper: (T) -> R): UiState<R> = when (this) {
    is DomainLocalState.Success -> UiState.Success(mapper(data))
    is DomainLocalState.Empty -> UiState.Empty
    is DomainLocalState.Error -> UiState.Error(message = message, cause = exception)
}

/**
 * Turns a stream of local results into a stream of [UiState]: [UiState.Loading] first, then one
 * state per result. It is cold and has no thread of its own, so the caller decides where to
 * collect it and how to hold the state.
 */
fun <T, R> Flow<DomainLocalState<T>>.asUiState(mapper: (T) -> R): Flow<UiState<R>> =
    map { it.toUiState(mapper) }.onStart { emit(UiState.Loading) }

/**
 * Runs [call] once when collected: [UiState.Loading] first, then the mapped result. Like
 * [asUiState] it is cold and has no thread of its own.
 */
fun <T, R> loadAsUiState(
    mapper: (T) -> R,
    call: suspend () -> DomainNetworkState<T>
): Flow<UiState<R>> = flow {
    emit(UiState.Loading)
    emit(catchAsNetworkError { call() }.toUiState(mapper))
}
