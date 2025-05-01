package dev.fizcode.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.fizcode.anime.navigation.AnimeBaseRoute
import dev.fizcode.anime.navigation.animeNavGraph
import dev.fizcode.designsystem.animation.navHostEnterTransition
import dev.fizcode.designsystem.animation.navHostExitTransition
import dev.fizcode.navigation.route.DashboardRoute
import kotlinx.serialization.Serializable

@Composable
fun NavigationBarNavGraph(
    navHostController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navHostController,
        startDestination = AnimeBaseRoute,
        popEnterTransition = { navHostEnterTransition() },
        popExitTransition = { navHostExitTransition() }
    ) {
        animeNavGraph(innerPadding)
        seasonalNavGraph(innerPadding)
        composable<MangaRoute> {
            Text(text = "Manga")
        }
        composable<BookmarkRoute> {
            Text(text = "Bookmark")
        }
    }
}

// TODO: Delete All serializable after there is a screen on each feature
@Serializable
data object SeasonalRoute: DashboardRoute

@Serializable
data object MangaRoute: DashboardRoute

@Serializable
data object BookmarkRoute: DashboardRoute

fun NavGraphBuilder.seasonalNavGraph(innerPadding: PaddingValues) {
    composable<SeasonalRoute> {
        Text(text = "Seasonal")
    }
}
