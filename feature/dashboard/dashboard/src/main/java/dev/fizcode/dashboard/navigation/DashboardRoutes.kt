package dev.fizcode.dashboard.navigation

import androidx.navigation3.runtime.NavKey
import dev.fizcode.anime.api.AnimeRoute
import dev.fizcode.bookmark.api.BookmarkRoute
import kotlinx.serialization.Serializable

// TODO: Delete Seasonal and Manga after there is a screen on each feature
@Serializable
data object SeasonalRoute : NavKey

@Serializable
data object MangaRoute : NavKey

/** Routes shown in the dashboard bottom bar, in display order. The first one is the start route. */
val dashboardTopLevelRoutes: Set<NavKey> = linkedSetOf(
    AnimeRoute,
    SeasonalRoute,
    MangaRoute,
    BookmarkRoute
)
