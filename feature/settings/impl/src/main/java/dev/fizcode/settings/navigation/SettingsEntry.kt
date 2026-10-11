package dev.fizcode.settings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.navigation.Navigator
import dev.fizcode.settings.api.SettingsRoute
import dev.fizcode.settings.presentation.SettingsScreen

fun EntryProviderScope<NavKey>.settingsEntry(navigator: Navigator) {
    entry<SettingsRoute> {
        SettingsScreen(onBackClick = navigator::goBack)
    }
}
