package dev.fizcode.common.base.handler

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.fizcode.common.base.responsehandler.UiState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

suspend fun <T> MutableStateFlow<UiState<T>>.asMutableStateFlow(
    dataCall: suspend () -> T
) {
    this.update { UiState.Loading }
    try {
        val data = dataCall.invoke()
        if (data != null) {
            this.update { UiState.Success(data) }
        } else {
            println("asMutableStateFlow(Empty)")
            this.update { UiState.Empty }
        }
    } catch (e: RedirectResponseException) {
        // 3xx - responses
        println("asMutableStateFlow(Error: $e)")
        this.update { UiState.ErrorRedirectResponse(error = e, message = e.message) }
    } catch (e: ClientRequestException) {
        // 4xx - responses
        println("asMutableStateFlow(Error: $e)")
        this.update { UiState.ErrorClientRequest(error = e, message = e.message) }
    } catch (e: ServerResponseException) {
        // 5xx - responses
        println("asMutableStateFlow(Error: $e)")
        this.update { UiState.ErrorServerResponse(error = e, message = e.message) }
    } catch (e: Exception) {
        println("asMutableStateFlow(Error: $e)")
        this.update { UiState.ErrorException(error = e, message = e.message) }
    }
}

suspend fun <T> Channel<UiState<T>>.asOneTimeEventFlow(
    dataCall: suspend () -> T
) {
    this.send(UiState.Loading)
    try {
        val data = dataCall.invoke()
        if (data != null) {
            this.send(UiState.Success(data))
        } else {
            this.send(UiState.Empty)
        }
    } catch (e: RedirectResponseException) {
        // 3xx - responses
        this.send(UiState.ErrorRedirectResponse(error = e, message = e.message))
    } catch (e: ClientRequestException) {
        // 4xx - responses
        this.send(UiState.ErrorClientRequest(error = e, message = e.message))
    } catch (e: ServerResponseException) {
        // 5xx - responses
        this.send(UiState.ErrorServerResponse(error = e, message = e.message))
    } catch (e: Exception) {
        this.send(UiState.ErrorException(error = e, message = e.message))
    }
}

suspend fun <T> MutableSharedFlow<UiState<T>>.asMutableSharedFlow(
    dataCall: suspend () -> T
) {
    this.emit(UiState.Loading)
    try {
        val data = dataCall.invoke()
        if (data != null) {
            this.emit(UiState.Success(data))
        } else {
            this.emit(UiState.Empty)
        }
        this.emit(UiState.Success(data))
    } catch (e: RedirectResponseException) {
        // 3xx - responses
        this.emit(UiState.ErrorRedirectResponse(error = e, message = e.message))
    } catch (e: ClientRequestException) {
        // 4xx - responses
        this.emit(UiState.ErrorClientRequest(error = e, message = e.message))
    } catch (e: ServerResponseException) {
        // 5xx - responses
        this.emit(UiState.ErrorServerResponse(error = e, message = e.message))
    } catch (e: Exception) {
        this.emit(UiState.ErrorException(error = e, message = e.message))
    }
}

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}
