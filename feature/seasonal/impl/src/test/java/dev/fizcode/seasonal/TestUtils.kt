package dev.fizcode.seasonal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/** Replaces Dispatchers.Main so `viewModelScope` works in plain JVM tests. */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule : TestWatcher() {
    override fun starting(description: Description) = Dispatchers.setMain(UnconfinedTestDispatcher())
    override fun finished(description: Description) = Dispatchers.resetMain()
}

/**
 * ViewModels hop to `Dispatchers.IO` internally, so virtual time does not apply:
 * wait (real time, bounded) until the flow emits a value matching [predicate].
 */
internal suspend fun <T> StateFlow<T>.awaitValue(
    timeoutMillis: Long = 5_000,
    predicate: (T) -> Boolean
): T = withTimeout(timeoutMillis) { first(predicate) }
