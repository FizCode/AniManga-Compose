package dev.fizcode.settings.di

import dev.fizcode.settings.data.SettingsRepositoryImpl
import dev.fizcode.settings.domain.repository.SettingsRepository
import dev.fizcode.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun settingsKoinModule() = module {
    viewModelOf(::SettingsViewModel)
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class
}
