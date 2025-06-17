package dev.fizcode.anime.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.fizcode.anime.presentation.AnimeScreen
import dev.fizcode.navigation.route.DashboardRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.animeNavGraph(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
    ) {
    navigation<AnimeBaseRoute>(startDestination = AnimeRoute) {
        composable<AnimeRoute> {
            AnimeScreen(
                onCardClick = onCardClick
            )
        }
    }
}

fun NavController.navigateToAnimeScreen(navOptions: NavOptions) =
    navigate(route = AnimeRoute, navOptions)

@Serializable
data object AnimeRoute : DashboardRoute

@Serializable
data object AnimeBaseRoute : DashboardRoute
