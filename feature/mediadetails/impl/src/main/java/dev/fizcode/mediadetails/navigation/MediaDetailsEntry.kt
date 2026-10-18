package dev.fizcode.mediadetails.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.mediadetails.api.MangaCharactersRoute
import dev.fizcode.mediadetails.api.MediaCastRoute
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.mediadetails.api.MediaStaffRoute
import dev.fizcode.mediadetails.presentation.MediaDetailsScreen
import dev.fizcode.mediadetails.presentation.people.MangaCharactersScreen
import dev.fizcode.mediadetails.presentation.people.MediaCastScreen
import dev.fizcode.mediadetails.presentation.people.MediaStaffScreen
import dev.fizcode.navigation.Navigator

fun EntryProviderScope<NavKey>.mediaDetailsEntry(navigator: Navigator) {
    entry<MediaDetailsRoute> { route ->
        MediaDetailsScreen(
            mediaType = route.mediaType,
            mediaId = route.mediaId,
            onBackPressed = navigator::goBack,
            onOpenCast = { navigator.navigate(MediaCastRoute(route.mediaId)) },
            onOpenStaff = { navigator.navigate(MediaStaffRoute(route.mediaId)) },
            onOpenCharacters = { navigator.navigate(MangaCharactersRoute(route.mediaId)) }
        )
    }
    entry<MediaCastRoute> { route ->
        MediaCastScreen(mediaId = route.mediaId, onBackClick = navigator::goBack)
    }
    entry<MediaStaffRoute> { route ->
        MediaStaffScreen(mediaId = route.mediaId, onBackClick = navigator::goBack)
    }
    entry<MangaCharactersRoute> { route ->
        MangaCharactersScreen(mediaId = route.mediaId, onBackClick = navigator::goBack)
    }
}
