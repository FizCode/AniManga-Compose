package dev.fizcode.mediadetails.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.mediadetails.presentation.MediaDetailsScreen
import dev.fizcode.navigation.Navigator

fun EntryProviderScope<NavKey>.mediaDetailsEntry(navigator: Navigator) {
    entry<MediaDetailsRoute> { route ->
        MediaDetailsScreen(
            mediaType = route.mediaType,
            mediaId = route.mediaId,
            onBackPressed = navigator::goBack
        )
    }
}
