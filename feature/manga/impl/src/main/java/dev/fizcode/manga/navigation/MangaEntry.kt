package dev.fizcode.manga.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.manga.api.MangaRoute
import dev.fizcode.manga.presentation.MangaScreen
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.navigation.Navigator
import dev.fizcode.navigation.topLevelEntryMetadata

fun EntryProviderScope<NavKey>.mangaEntry(navigator: Navigator) {
    entry<MangaRoute>(metadata = topLevelEntryMetadata()) {
        MangaScreen(
            onCardClick = { mediaType, mediaId ->
                navigator.navigate(MediaDetailsRoute(mediaType = mediaType, mediaId = mediaId))
            }
        )
    }
}
