package dev.fizcode.dashboard.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.designsystem.component.card.ReservedComponent
import dev.fizcode.navigation.topLevelEntryMetadata

/** Placeholder entry for the dashboard tab that doesn't have its own feature yet. */
fun EntryProviderScope<NavKey>.dashboardEntry() {
    entry<MangaRoute>(metadata = topLevelEntryMetadata()) {
        ReservedComponent(modifier = Modifier.fillMaxSize())
    }
}
