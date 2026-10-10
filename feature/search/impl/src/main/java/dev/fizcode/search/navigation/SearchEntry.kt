package dev.fizcode.search.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.navigation.Navigator
import dev.fizcode.search.api.SearchRoute
import dev.fizcode.search.presentation.SearchScreen

fun EntryProviderScope<NavKey>.searchEntry(navigator: Navigator) {
    entry<SearchRoute> {
        SearchScreen(
            onCardClick = { mediaType, mediaId ->
                navigator.navigate(MediaDetailsRoute(mediaType = mediaType, mediaId = mediaId))
            }
        )
    }
}
