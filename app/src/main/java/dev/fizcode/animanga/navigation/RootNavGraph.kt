package dev.fizcode.animanga.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.fizcode.dashboard.navigation.dashboardNavGraph
import dev.fizcode.dashboard.navigation.navigateToDashboardScreen
import dev.fizcode.navigation.route.InitialRoute
import dev.fizcode.onboarding.navigation.onBoardingNavGraph

@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    startDestination: InitialRoute
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
        dashboardNavGraph()
    }
}
