package dev.fizcode.dashboard.navigation

import androidx.navigation3.runtime.NavKey
import dev.fizcode.anime.api.AnimeRoute
import dev.fizcode.bookmark.api.BookmarkRoute
import dev.fizcode.manga.api.MangaRoute
import dev.fizcode.seasonal.api.SeasonalRoute

/** Routes shown in the dashboard bottom bar, in display order. The first one is the start route. */
val dashboardTopLevelRoutes: Set<NavKey> = linkedSetOf(
    AnimeRoute,
    SeasonalRoute,
    MangaRoute,
    BookmarkRoute
)
