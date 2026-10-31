package dev.fizcode.common.base.callhandler

import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> catchAsNetworkError(
    block: () -> DomainNetworkState<T>
): DomainNetworkState<T> = try {
    block()
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    DomainNetworkState.ErrorNetwork(exception = e, message = e.message)
}
