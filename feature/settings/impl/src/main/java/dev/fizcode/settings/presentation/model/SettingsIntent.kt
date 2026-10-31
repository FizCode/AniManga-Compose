package dev.fizcode.settings.presentation.model

import dev.fizcode.common.base.mvi.UiIntent
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle

internal sealed interface SettingsIntent : UiIntent {
    data class SetThemeMode(val value: ThemeMode) : SettingsIntent
    data class SetTitleStyle(val value: TitleStyle) : SettingsIntent
    data class SetNsfwFilter(val value: NsfwFilter) : SettingsIntent
}
