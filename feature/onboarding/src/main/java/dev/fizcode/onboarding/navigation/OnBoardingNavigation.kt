package dev.fizcode.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.fizcode.navigation.route.InitialRoute
import dev.fizcode.onboarding.presentation.presentation.OnBoardingScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.onBoardingNavGraph(
    onClickSkip: () -> Unit
) {
    composable<OnBoardingRoute> {
        OnBoardingScreen(onClickSkip = onClickSkip)
    }
}

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) =
    navigate(route = OnBoardingRoute, navOptions)

@Serializable
data object OnBoardingRoute : InitialRoute
