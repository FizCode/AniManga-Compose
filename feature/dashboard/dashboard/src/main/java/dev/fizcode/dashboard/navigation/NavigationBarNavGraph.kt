package dev.fizcode.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.fizcode.anime.navigation.AnimeBaseRoute
import dev.fizcode.anime.navigation.animeNavGraph
import dev.fizcode.designsystem.animation.navHostEnterTransition
import dev.fizcode.designsystem.animation.navHostExitTransition
import dev.fizcode.designsystem.component.card.ReservedComponent
import dev.fizcode.navigation.route.DashboardRoute
import kotlinx.serialization.Serializable

@Composable
fun NavigationBarNavGraph(
    navHostController: NavHostController,
    innerPadding: PaddingValues,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navHostController,
        startDestination = AnimeBaseRoute,
        popEnterTransition = { navHostEnterTransition() },
        popExitTransition = { navHostExitTransition() }
    ) {
        animeNavGraph(
            onCardClick = onCardClick
        )
        seasonalNavGraph()
        composable<MangaRoute> {
            ReservedComponent(modifier = Modifier.fillMaxSize())
        }
        composable<BookmarkRoute> {
            ReservedComponent(modifier = Modifier.fillMaxSize())
        }
    }
}

// TODO: Delete All serializable after there is a screen on each feature
@Serializable
data object SeasonalRoute : DashboardRoute

@Serializable
data object MangaRoute : DashboardRoute

@Serializable
data object BookmarkRoute : DashboardRoute

fun NavGraphBuilder.seasonalNavGraph() {
    composable<SeasonalRoute> {
        ReservedComponent(modifier = Modifier.fillMaxSize())
    }
}
