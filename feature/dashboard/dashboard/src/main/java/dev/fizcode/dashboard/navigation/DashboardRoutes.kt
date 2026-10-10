package dev.fizcode.dashboard.navigation

import androidx.navigation3.runtime.NavKey
import dev.fizcode.anime.api.AnimeRoute
import dev.fizcode.bookmark.api.BookmarkRoute
import dev.fizcode.seasonal.api.SeasonalRoute
import kotlinx.serialization.Serializable

// TODO: Delete Manga after it has its own feature
@Serializable
data object MangaRoute : NavKey

/** Routes shown in the dashboard bottom bar, in display order. The first one is the start route. */
val dashboardTopLevelRoutes: Set<NavKey> = linkedSetOf(
    AnimeRoute,
    SeasonalRoute,
    MangaRoute,
    BookmarkRoute
)
