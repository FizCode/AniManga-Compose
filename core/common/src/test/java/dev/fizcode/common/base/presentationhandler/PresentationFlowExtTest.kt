package dev.fizcode.common.base.presentationhandler

import dev.fizcode.common.base.callhandler.DomainLocalState
import dev.fizcode.common.base.callhandler.UiState
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class PresentationFlowExtTest {

    @Test
    fun `loading comes first then one state per result`() = runBlocking {
        val failure = IllegalStateException("db down")

        val states = flowOf(
            DomainLocalState.Success(2),
            DomainLocalState.Empty,
            DomainLocalState.Error(failure)
        ).asUiState { it * 10 }.toList()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(20),
                UiState.Empty,
                UiState.Error(message = "db down", cause = failure)
            ),
            states
        )
    }
}
