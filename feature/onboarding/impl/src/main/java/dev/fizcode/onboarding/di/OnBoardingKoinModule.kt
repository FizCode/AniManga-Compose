package dev.fizcode.onboarding.di

import dev.fizcode.onboarding.api.OnBoardingStatus
import dev.fizcode.onboarding.data.OnBoardingStatusImpl
import dev.fizcode.onboarding.presentation.OnBoardingViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun onBoardingKoinModule() = module {
    singleOf(::OnBoardingStatusImpl) bind OnBoardingStatus::class
    viewModelOf(::OnBoardingViewModel)
}
