package dev.fizcode.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle
import dev.fizcode.settings.domain.repository.SettingsRepository
import dev.fizcode.settings.util.Constant
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.DEFAULT,
    val titleStyle: TitleStyle = TitleStyle.DEFAULT,
    val nsfwFilter: NsfwFilter = NsfwFilter.SAFE
)

internal class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.themeMode,
        settingsRepository.titleStyle,
        settingsRepository.nsfwFilter,
        ::SettingsUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(Constant.STOP_TIMEOUT_MILLIS),
        initialValue = SettingsUiState()
    )

    fun setThemeMode(value: ThemeMode) {
        viewModelScope.launch { settingsRepository.setThemeMode(value) }
    }

    fun setTitleStyle(value: TitleStyle) {
        viewModelScope.launch { settingsRepository.setTitleStyle(value) }
    }

    fun setNsfwFilter(value: NsfwFilter) {
        viewModelScope.launch { settingsRepository.setNsfwFilter(value) }
    }
}
