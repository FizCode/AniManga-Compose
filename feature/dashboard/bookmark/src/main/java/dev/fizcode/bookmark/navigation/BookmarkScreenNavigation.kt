package dev.fizcode.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import dev.fizcode.bookmark.presentation.BookmarkScreen
import dev.fizcode.navigation.route.DashboardRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.bookmarkNavGraph(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) = composable<BookmarkRoute> {
    BookmarkScreen(
        onCardClick = onCardClick
    )
}

fun NavController.navigateToBookmarkScreen(
    navOptions: () -> Unit = { navOptions {} }
) = navigate(route = BookmarkRoute) { navOptions() }

@Serializable
data object BookmarkRoute : DashboardRoute
