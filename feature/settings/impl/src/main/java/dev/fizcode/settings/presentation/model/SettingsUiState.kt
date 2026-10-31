package dev.fizcode.settings.presentation.model

import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle

internal data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.DEFAULT,
    val titleStyle: TitleStyle = TitleStyle.DEFAULT,
    val nsfwFilter: NsfwFilter = NsfwFilter.SAFE
)
