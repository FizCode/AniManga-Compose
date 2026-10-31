package dev.fizcode.onboarding.presentation

import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.onboarding.api.OnBoardingStatus
import dev.fizcode.onboarding.presentation.model.OnBoardingEffect
import dev.fizcode.onboarding.presentation.model.OnBoardingIntent
import dev.fizcode.onboarding.presentation.model.OnBoardingUiState
import kotlinx.coroutines.launch

internal class OnBoardingViewModel(
    private val onBoardingStatus: OnBoardingStatus
) : MviViewModel<OnBoardingUiState, OnBoardingIntent, OnBoardingEffect>(OnBoardingUiState()) {

    override fun onIntent(intent: OnBoardingIntent) = when (intent) {
        OnBoardingIntent.Complete -> complete()
    }

    private fun complete() {
        if (uiState.value.isSaving) return
        setState { copy(isSaving = true) }
        viewModelScope.launch {
            onBoardingStatus.markCompleted()
            sendEffect(OnBoardingEffect.Finished)
        }
    }
}
