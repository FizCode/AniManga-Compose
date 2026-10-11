package dev.fizcode.onboarding.presentation

import androidx.lifecycle.ViewModel
import dev.fizcode.onboarding.api.OnBoardingStatus

internal class OnBoardingViewModel(
    private val onBoardingStatus: OnBoardingStatus
) : ViewModel() {

    /** Remembers that the onboarding was seen so it is not shown again on the next launch. */
    suspend fun completeOnBoarding() = onBoardingStatus.markCompleted()
}
