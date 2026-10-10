package dev.fizcode.seasonal.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.navigation.Navigator
import dev.fizcode.navigation.topLevelEntryMetadata
import dev.fizcode.seasonal.api.SeasonalRoute
import dev.fizcode.seasonal.presentation.SeasonalScreen

fun EntryProviderScope<NavKey>.seasonalEntry(navigator: Navigator) {
    entry<SeasonalRoute>(metadata = topLevelEntryMetadata()) {
        SeasonalScreen(
            onCardClick = { mediaType, mediaId ->
                navigator.navigate(MediaDetailsRoute(mediaType = mediaType, mediaId = mediaId))
            }
        )
    }
}
