package dev.fizcode.common.base.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.loadAsUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface UiIntent

interface UiEffect

/**
 * Base for screens that follow Model-View-Intent: the screen sends [UiIntent]s through [onIntent],
 * reads a single immutable [uiState], and receives one-off [UiEffect]s from [effect].
 *
 * Screens without one-off events use [Nothing] for [E].
 */
abstract class MviViewModel<S, I : UiIntent, E : UiEffect>(initialState: S) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun onIntent(intent: I)

    protected fun setState(reduce: S.() -> S) = _uiState.update { it.reduce() }

    protected fun <T> Flow<T>.collectIntoState(reduce: S.(T) -> S) {
        onEach { value -> setState { reduce(value) } }.launchIn(viewModelScope)
    }

    protected fun <T, R> loadSection(
        reloads: Flow<Unit>,
        mapper: (T) -> R,
        call: suspend () -> DomainNetworkState<T>,
        reduce: S.(UiState<R>) -> S
    ) {
        reloads
            .onStart { emit(Unit) }
            .flatMapLatest { loadAsUiState(mapper, call) }
            .collectIntoState(reduce)
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
