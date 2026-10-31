package dev.fizcode.common.base.callhandler

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class CatchAsNetworkErrorTest {

    @Test
    fun `returns the result when nothing is thrown`() = runBlocking {
        val result = catchAsNetworkError { DomainNetworkState.Success("data") }

        assertEquals(DomainNetworkState.Success("data"), result)
    }

    @Test
    fun `wraps an exception as a network error`() = runBlocking {
        val failure = IllegalStateException("boom")

        val result = catchAsNetworkError<String> { throw failure }

        val error = result as DomainNetworkState.ErrorNetwork
        assertSame(failure, error.exception)
        assertEquals("boom", error.message)
    }

    @Test
    fun `rethrows cancellation instead of turning it into an error`() {
        val cancellation = CancellationException("cancelled")

        val thrown = runCatching {
            runBlocking { catchAsNetworkError<String> { throw cancellation } }
        }.exceptionOrNull()

        assertTrue(thrown is CancellationException)
    }
}
