package dev.fizcode.animanga.model

import dev.fizcode.dashboard.navigation.DashboardRoute
import dev.fizcode.navigation.route.RootRoute
import dev.fizcode.onboarding.navigation.OnBoardingRoute

sealed class InitialDestinationRoute(
    val route: RootRoute
) {

    data object OnBoarding : InitialDestinationRoute(route = OnBoardingRoute)

    data object Dashboard : InitialDestinationRoute(route = DashboardRoute)
}
