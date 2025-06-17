package dev.fizcode.animanga.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.fizcode.animanga.util.Constant
import dev.fizcode.dashboard.navigation.dashboardNavGraph
import dev.fizcode.dashboard.navigation.navigateToDashboardScreen
import dev.fizcode.mediadetails.navigation.mediaDetailsNavGraph
import dev.fizcode.mediadetails.navigation.navigateToMediaDetailsScreen
import dev.fizcode.navigation.route.RootRoute
import dev.fizcode.onboarding.navigation.onBoardingNavGraph

@Suppress("FunctionNaming")
@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    startDestination: RootRoute
) {
    val enterTransition = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(Constant.ANIMATION_DURATION)
    )
    val exitTransition = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(Constant.ANIMATION_DURATION)
    )
    val popEnterTransition = slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(Constant.ANIMATION_DURATION)
    )
    val popExitTransition = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(Constant.ANIMATION_DURATION)
    )

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition },
        popEnterTransition = { popEnterTransition },
        popExitTransition = { popExitTransition }

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
        mediaDetailsNavGraph(
            onBackPressed = navHostController::popBackStack
        )
    }
}
