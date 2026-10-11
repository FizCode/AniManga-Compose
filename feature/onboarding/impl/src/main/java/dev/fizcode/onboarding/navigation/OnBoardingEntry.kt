package dev.fizcode.onboarding.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.onboarding.api.OnBoardingRoute
import dev.fizcode.onboarding.presentation.OnBoardingScreen

fun EntryProviderScope<NavKey>.onBoardingEntry(onFinished: () -> Unit) {
    entry<OnBoardingRoute> { OnBoardingScreen(onFinished = onFinished) }
}
