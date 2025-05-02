package dev.fizcode.animanga.model

import dev.fizcode.dashboard.navigation.DashboardRoute
import dev.fizcode.navigation.route.InitialRoute
import dev.fizcode.onboarding.navigation.OnBoardingRoute

sealed class InitialDestinationRoute(
    val route: InitialRoute
) {

    data object OnBoarding : InitialDestinationRoute(route = OnBoardingRoute)

    data object Dashboard : InitialDestinationRoute(route = DashboardRoute)
}
