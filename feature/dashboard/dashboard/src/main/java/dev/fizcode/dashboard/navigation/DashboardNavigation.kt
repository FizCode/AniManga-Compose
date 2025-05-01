package dev.fizcode.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.fizcode.dashboard.presentation.DashboardScreen
import dev.fizcode.navigation.route.InitialRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.dashboardNavGraph() {
    composable<DashboardRoute> {
        DashboardScreen()
    }
}

fun NavController.navigateToDashboardScreen(navOptions: NavOptions? = null) =
    navigate(route = DashboardRoute, navOptions)

@Serializable
data object DashboardRoute : InitialRoute
