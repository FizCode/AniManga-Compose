package dev.fizcode.animanga.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.fizcode.dashboard.navigation.dashboardNavGraph
import dev.fizcode.dashboard.navigation.navigateToDashboardScreen
import dev.fizcode.mediadetails.navigation.mediaDetailsNavGraph
import dev.fizcode.mediadetails.navigation.navigateToMediaDetailsScreen
import dev.fizcode.navigation.route.RootRoute
import dev.fizcode.onboarding.navigation.onBoardingNavGraph

@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    startDestination: RootRoute
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        onBoardingNavGraph(
            onClickSkip = {
                navHostController.popBackStack()
                navHostController.navigateToDashboardScreen()
            }
        )
        dashboardNavGraph(
            onCardClick = navHostController::navigateToMediaDetailsScreen
        )
        mediaDetailsNavGraph()
    }
}
