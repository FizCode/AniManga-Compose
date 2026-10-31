package dev.fizcode.onboarding.presentation.model

import dev.fizcode.common.base.mvi.UiIntent

internal sealed interface OnBoardingIntent : UiIntent {
    data object Complete : OnBoardingIntent
}
