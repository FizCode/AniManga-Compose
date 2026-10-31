package dev.fizcode.network

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.util.ErrorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ResponseExtTest {

    @Test
    fun `non empty result is a success`() {
        assertEquals(DomainNetworkState.Success(listOf(1)), processResponse { listOf(1) })
    }

    @Test
    fun `empty list, blank string and null are empty`() {
        assertEquals(DomainNetworkState.Empty, processResponse { emptyList<Int>() })
        assertEquals(DomainNetworkState.Empty, processResponse { " " })
        assertEquals(DomainNetworkState.Empty, processResponse<String?> { null })
    }

    @Test
    fun `io failure is a network error`() {
        val result = processResponse<String> { throw IOException("offline") }

        assertEquals(ErrorType.Network, (result as DomainNetworkState.ErrorNetwork).type)
    }

    @Test
    fun `unknown failure keeps its message`() {
        val result = processResponse<String> { throw IllegalStateException("boom") }

        val error = result as DomainNetworkState.ErrorNetwork
        assertEquals(ErrorType.Unknown, error.type)
        assertEquals("boom", error.message)
    }

    @Test
    fun `cancellation is rethrown instead of becoming an error`() {
        val thrown = runCatching {
            processResponse<String> { throw CancellationException("cancelled") }
        }.exceptionOrNull()

        assertTrue(thrown is CancellationException)
    }
}
