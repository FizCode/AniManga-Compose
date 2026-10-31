package dev.fizcode.onboarding.presentation.model

import dev.fizcode.common.base.mvi.UiEffect

internal sealed interface OnBoardingEffect : UiEffect {
    data object Finished : OnBoardingEffect
}
