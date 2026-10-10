package dev.fizcode.mediadetails.data

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.data.repository.orFallback
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class OrFallbackTest {

    private val primaryError = DomainNetworkState.ErrorNetwork(message = "jikan down")
    private val fallbackError = DomainNetworkState.ErrorNetwork(message = "anilist down")

    @Test
    fun `a successful primary result skips the fallback`() = runBlocking {
        var fallbackCalled = false

        val result = DomainNetworkState.Success(listOf(1)).orFallback {
            fallbackCalled = true
            DomainNetworkState.Success(listOf(2))
        }

        assertEquals(DomainNetworkState.Success(listOf(1)), result)
        assertEquals(false, fallbackCalled)
    }

    @Test
    fun `a primary error uses a successful fallback`() = runBlocking {
        val result = primaryError.orFallback { DomainNetworkState.Success(listOf(2)) }

        assertEquals(DomainNetworkState.Success(listOf(2)), result)
    }

    @Test
    fun `an empty primary result also uses a successful fallback`() = runBlocking {
        val primary: DomainNetworkState<List<Int>> = DomainNetworkState.Empty

        val result = primary.orFallback { DomainNetworkState.Success(listOf(2)) }

        assertEquals(DomainNetworkState.Success(listOf(2)), result)
    }

    @Test
    fun `when both fail the primary error is returned`() = runBlocking {
        val result = primaryError.orFallback { fallbackError }

        assertSame(primaryError, result)
    }

    @Test
    fun `an empty fallback keeps the primary error`() = runBlocking {
        val result: DomainNetworkState<List<Int>> = primaryError.orFallback { DomainNetworkState.Empty }

        assertSame(primaryError, result)
        assertTrue(result is DomainNetworkState.ErrorNetwork)
    }
}
