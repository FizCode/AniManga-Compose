package dev.fizcode.animanga.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.fizcode.anime.api.AnimeRoute
import dev.fizcode.anime.navigation.animeEntry
import dev.fizcode.animanga.util.Constant
import dev.fizcode.bookmark.navigation.bookmarkEntry
import dev.fizcode.dashboard.navigation.dashboardEntry
import dev.fizcode.dashboard.navigation.dashboardTopLevelRoutes
import dev.fizcode.dashboard.presentation.NavigationBarComponent
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.mediadetails.navigation.mediaDetailsEntry
import dev.fizcode.navigation.Navigator
import dev.fizcode.navigation.rememberNavigationState
import dev.fizcode.navigation.toEntries
import dev.fizcode.onboarding.navigation.onBoardingEntry
import dev.fizcode.search.navigation.searchEntry

/**
 * Hosts every destination of the app and the dashboard bottom bar.
 *
 * @param deepLinkRoute a route requested from outside the app (e.g. a web link) to open on top
 * of the current screen. [onDeepLinkHandled] is called once it has been navigated to.
 */
@Composable
fun AppNavDisplay(
    deepLinkRoute: MediaDetailsRoute?,
    onDeepLinkHandled: () -> Unit
) {
    val navigationState = rememberNavigationState(
        startRoute = AnimeRoute,
        topLevelRoutes = dashboardTopLevelRoutes
    )
    val navigator = remember(navigationState) { Navigator(navigationState) }

    LaunchedEffect(deepLinkRoute) {
        if (deepLinkRoute != null) {
            navigator.navigate(deepLinkRoute)
            onDeepLinkHandled()
        }
    }

    val entryProvider = remember(navigator) {
        entryProvider<NavKey> {
            animeEntry(navigator)
            dashboardEntry()
            bookmarkEntry(navigator)
            searchEntry(navigator)
            mediaDetailsEntry(navigator)
            onBoardingEntry(onClickSkip = navigator::goBack)
        }
    }

    Scaffold(
        bottomBar = {
            if (navigationState.currentRoute in dashboardTopLevelRoutes) {
                NavigationBarComponent(
                    selectedRoute = navigationState.topLevelRoute,
                    onItemClick = navigator::navigate
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            entries = navigationState.toEntries(entryProvider),
            onBack = navigator::goBack,
            transitionSpec = { forwardTransition },
            popTransitionSpec = { backwardTransition },
            predictivePopTransitionSpec = { backwardTransition }
        )
    }
}

private val forwardTransition = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(Constant.ANIMATION_DURATION)
) togetherWith slideOutHorizontally(
    targetOffsetX = { -it },
    animationSpec = tween(Constant.ANIMATION_DURATION)
)

private val backwardTransition = slideInHorizontally(
    initialOffsetX = { -it },
    animationSpec = tween(Constant.ANIMATION_DURATION)
) togetherWith slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(Constant.ANIMATION_DURATION)
)
