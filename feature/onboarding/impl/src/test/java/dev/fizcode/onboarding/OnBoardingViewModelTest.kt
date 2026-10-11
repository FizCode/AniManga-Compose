package dev.fizcode.onboarding

import dev.fizcode.onboarding.api.OnBoardingStatus
import dev.fizcode.onboarding.presentation.OnBoardingViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnBoardingViewModelTest {

    private class FakeOnBoardingStatus : OnBoardingStatus {
        var completed = false

        override suspend fun isCompleted() = completed

        override suspend fun markCompleted() {
            completed = true
        }
    }

    @Test
    fun `completing the onboarding stores that it was seen`() = runBlocking {
        val status = FakeOnBoardingStatus()
        assertFalse(status.isCompleted())

        OnBoardingViewModel(status).completeOnBoarding()

        assertTrue(status.isCompleted())
    }
}
