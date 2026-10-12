package dev.fizcode.bookmark.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.bookmark.api.BookmarkRoute
import dev.fizcode.bookmark.presentation.BookmarkScreen
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.navigation.Navigator
import dev.fizcode.navigation.topLevelEntryMetadata

fun EntryProviderScope<NavKey>.bookmarkEntry(navigator: Navigator) {
    entry<BookmarkRoute>(metadata = topLevelEntryMetadata()) {
        BookmarkScreen(
            onCardClick = { mediaType, mediaId ->
                navigator.navigate(MediaDetailsRoute(mediaType = mediaType, mediaId = mediaId))
            }
        )
    }
}
