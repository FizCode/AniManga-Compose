package dev.fizcode.common.base.callhandler

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalDatabaseExtTest {

    @Test
    fun `empty collections, maps, blank strings and null are empty`() {
        assertTrue(isEmptyResult(null))
        assertTrue(isEmptyResult(emptyList<Int>()))
        assertTrue(isEmptyResult(emptyMap<Int, Int>()))
        assertTrue(isEmptyResult("  "))
        assertFalse(isEmptyResult(listOf(1)))
        assertFalse(isEmptyResult(0))
    }

    @Test
    fun `emissions become success or empty`() = runBlocking {
        val states = processDatabaseCall { flowOf(listOf(1), emptyList()) }.toList()

        assertEquals(listOf(DomainLocalState.Success(listOf(1)), DomainLocalState.Empty), states)
    }

    @Test
    fun `a failing source becomes an error state`() = runBlocking {
        val failure = IllegalStateException("db down")

        val states = processDatabaseCall<List<Int>> { flow { throw failure } }.toList()

        val error = states.single() as DomainLocalState.Error
        assertEquals(failure, error.exception)
        assertEquals("db down", error.message)
    }
}
