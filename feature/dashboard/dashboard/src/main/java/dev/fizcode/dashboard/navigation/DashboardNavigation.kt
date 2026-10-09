package dev.fizcode.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.fizcode.dashboard.presentation.DashboardScreen
import dev.fizcode.navigation.route.RootRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.dashboardNavGraph(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    onSearchClick: () -> Unit
) {
    composable<DashboardRoute> {
        DashboardScreen(
            navHostController = rememberNavController(),
            onCardClick = onCardClick,
            onSearchClick = onSearchClick
        )
    }
}

fun NavController.navigateToDashboardScreen(navOptions: NavOptions? = null) =
    navigate(route = DashboardRoute, navOptions)

@Serializable
data object DashboardRoute : RootRoute
