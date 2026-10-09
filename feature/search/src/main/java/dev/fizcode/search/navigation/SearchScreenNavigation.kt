package dev.fizcode.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import dev.fizcode.navigation.route.RootRoute
import dev.fizcode.search.presentation.SearchScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.searchNavGraph(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) = composable<SearchRoute> {
    SearchScreen(onCardClick = onCardClick)
}

fun NavController.navigateToSearchScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = SearchRoute) {
    navOptions()
}

@Serializable
data object SearchRoute : RootRoute
