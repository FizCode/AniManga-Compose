package dev.fizcode.dashboard.model

import androidx.compose.ui.graphics.vector.ImageVector
import dev.fizcode.dashboard.navigation.BookmarkRoute
import dev.fizcode.dashboard.navigation.MangaRoute
import dev.fizcode.dashboard.navigation.SeasonalRoute
import dev.fizcode.dashboard.util.Constant
import dev.fizcode.anime.navigation.AnimeRoute
import dev.fizcode.designsystem.icon.MaterialIcon
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
        icon = MaterialIcon.OUTLINED_MOVIE,
        selectedIcon = MaterialIcon.FILLED_MOVIE
    )

    data object Seasonal: DashboardDestinationItems(
        route = SeasonalRoute,
        title = Constant.TITLE_SEASONAL,
        icon = MaterialIcon.OUTLINED_WB_SUNNY,
        selectedIcon = MaterialIcon.ROUNDED_WB_SUNNY
    )

    data object Manga: DashboardDestinationItems(
        route = MangaRoute,
        title = Constant.TITLE_MANGA,
        icon = MaterialIcon.OUTLINED_BOOK,
        selectedIcon = MaterialIcon.ROUNDED_BOOK
    )

    data object Bookmark: DashboardDestinationItems(
        route = BookmarkRoute,
        title = Constant.TITLE_BOOKMARK,
        icon = MaterialIcon.ROUNDED_LIST_ALT,
        selectedIcon = MaterialIcon.FILLED_LIST_ALT
    )
}
