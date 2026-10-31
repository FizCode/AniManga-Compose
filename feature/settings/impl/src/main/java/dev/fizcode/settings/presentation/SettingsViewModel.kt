package dev.fizcode.settings.presentation

import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.settings.domain.repository.SettingsRepository
import dev.fizcode.settings.presentation.model.SettingsIntent
import dev.fizcode.settings.presentation.model.SettingsUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : MviViewModel<SettingsUiState, SettingsIntent, Nothing>(SettingsUiState()) {

    private val writes = Channel<SettingsIntent>(Channel.UNLIMITED)

    init { initial() }

    private fun initial() {
        combine(
            settingsRepository.themeMode,
            settingsRepository.titleStyle,
            settingsRepository.nsfwFilter,
            ::Triple
        ).collectIntoState { (themeMode, titleStyle, nsfwFilter) ->
            copy(themeMode = themeMode, titleStyle = titleStyle, nsfwFilter = nsfwFilter)
        }

        viewModelScope.launch {
            for (intent in writes) save(intent)
        }
    }

    override fun onIntent(intent: SettingsIntent) {
        writes.trySend(intent)
    }

    private suspend fun save(intent: SettingsIntent) = when (intent) {
        is SettingsIntent.SetThemeMode -> settingsRepository.setThemeMode(intent.value)
        is SettingsIntent.SetTitleStyle -> settingsRepository.setTitleStyle(intent.value)
        is SettingsIntent.SetNsfwFilter -> settingsRepository.setNsfwFilter(intent.value)
    }
}
