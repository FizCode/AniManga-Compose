package dev.fizcode.dashboard.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.designsystem.component.card.ReservedComponent
import dev.fizcode.navigation.topLevelEntryMetadata

/** Placeholder entries for the dashboard tabs that don't have their own feature yet. */
fun EntryProviderScope<NavKey>.dashboardEntry() {
    entry<SeasonalRoute>(metadata = topLevelEntryMetadata()) {
        ReservedComponent(modifier = Modifier.fillMaxSize())
    }
    entry<MangaRoute>(metadata = topLevelEntryMetadata()) {
        ReservedComponent(modifier = Modifier.fillMaxSize())
    }
}
