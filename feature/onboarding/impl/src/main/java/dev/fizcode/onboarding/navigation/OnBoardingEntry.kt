package dev.fizcode.onboarding.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.onboarding.api.OnBoardingRoute
import dev.fizcode.onboarding.presentation.presentation.OnBoardingScreen

fun EntryProviderScope<NavKey>.onBoardingEntry(onClickSkip: () -> Unit) {
    entry<OnBoardingRoute> { OnBoardingScreen(onClickSkip = onClickSkip) }
}
