package dev.fizcode.dashboard.model

import androidx.compose.ui.graphics.vector.ImageVector
import dev.fizcode.anime.navigation.AnimeRoute
import dev.fizcode.dashboard.navigation.BookmarkRoute
import dev.fizcode.dashboard.navigation.MangaRoute
import dev.fizcode.dashboard.navigation.SeasonalRoute
import dev.fizcode.dashboard.util.Constant
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.navigation.route.DashboardRoute

internal sealed class DashboardDestinationItems(
    val route: DashboardRoute,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Anime: DashboardDestinationItems(
        route = AnimeRoute,
        title = Constant.TITLE_ANIME,
        icon = CustomIcon.OUTL_MOVIE,
        selectedIcon = CustomIcon.FILL_MOVIE
    )

    data object Seasonal: DashboardDestinationItems(
        route = SeasonalRoute,
        title = Constant.TITLE_SEASONAL,
        icon = CustomIcon.OUTL_WB_SUNNY,
        selectedIcon = CustomIcon.ROUND_WB_SUNNY
    )

    data object Manga: DashboardDestinationItems(
        route = MangaRoute,
        title = Constant.TITLE_MANGA,
        icon = CustomIcon.OUTL_BOOK,
        selectedIcon = CustomIcon.ROUND_BOOK
    )

    data object Bookmark: DashboardDestinationItems(
        route = BookmarkRoute,
        title = Constant.TITLE_BOOKMARK,
        icon = CustomIcon.ROUND_LIST_ALT,
        selectedIcon = CustomIcon.FILL_LIST_ALT
    )
}
