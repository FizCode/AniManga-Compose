package dev.fizcode.onboarding

import dev.fizcode.onboarding.api.OnBoardingStatus
import dev.fizcode.onboarding.presentation.OnBoardingViewModel
import dev.fizcode.onboarding.presentation.model.OnBoardingEffect
import dev.fizcode.onboarding.presentation.model.OnBoardingIntent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class OnBoardingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeOnBoardingStatus(private val gate: CompletableDeferred<Unit>? = null) :
        OnBoardingStatus {
        var completed = false
        var markCalls = 0

        override suspend fun isCompleted() = completed

        override suspend fun markCompleted() {
            markCalls++
            gate?.await()
            completed = true
        }
    }

    @Test
    fun `completing stores that it was seen and then finishes`() = runBlocking {
        val status = FakeOnBoardingStatus()
        assertFalse(status.isCompleted())
        val viewModel = OnBoardingViewModel(status)

        viewModel.onIntent(OnBoardingIntent.Complete)

        assertEquals(OnBoardingEffect.Finished, viewModel.effect.first())
        assertTrue(status.isCompleted())
    }

    @Test
    fun `completing twice while saving saves and finishes once`() = runBlocking {
        val gate = CompletableDeferred<Unit>()
        val status = FakeOnBoardingStatus(gate)
        val viewModel = OnBoardingViewModel(status)

        viewModel.onIntent(OnBoardingIntent.Complete)
        viewModel.onIntent(OnBoardingIntent.Complete)
        gate.complete(Unit)

        assertEquals(OnBoardingEffect.Finished, viewModel.effect.first())
        assertEquals(1, status.markCalls)
    }
}
