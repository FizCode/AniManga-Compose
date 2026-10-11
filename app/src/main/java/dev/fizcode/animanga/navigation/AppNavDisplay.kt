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
import org.koin.compose.koinInject
import dev.fizcode.onboarding.api.OnBoardingStatus
import dev.fizcode.onboarding.api.OnBoardingRoute
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.fizcode.anime.api.AnimeRoute
import dev.fizcode.anime.navigation.animeEntry
import dev.fizcode.animanga.util.Constant
import dev.fizcode.bookmark.navigation.bookmarkEntry
import dev.fizcode.dashboard.navigation.dashboardTopLevelRoutes
import dev.fizcode.dashboard.presentation.NavigationBarComponent
import dev.fizcode.manga.navigation.mangaEntry
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.mediadetails.navigation.mediaDetailsEntry
import dev.fizcode.navigation.Navigator
import dev.fizcode.navigation.rememberNavigationState
import dev.fizcode.navigation.toEntries
import dev.fizcode.onboarding.navigation.onBoardingEntry
import dev.fizcode.search.navigation.searchEntry
import dev.fizcode.seasonal.navigation.seasonalEntry

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

    // The onboarding is only shown on the first launch, so nothing is displayed until it is known.
    val onBoardingStatus: OnBoardingStatus = koinInject()
    var isOnBoardingChecked by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val startStack = navigationState.backStacks.getValue(navigationState.startRoute)
        // A restored back stack can already contain it, e.g. after the process was killed.
        if (OnBoardingRoute !in startStack && !onBoardingStatus.isCompleted()) {
            navigator.navigate(OnBoardingRoute)
        }
        isOnBoardingChecked = true
    }

    // Waits for the onboarding check so a link opens on top of the onboarding, not below it.
    LaunchedEffect(deepLinkRoute, isOnBoardingChecked) {
        if (isOnBoardingChecked && deepLinkRoute != null) {
            navigator.navigate(deepLinkRoute)
            onDeepLinkHandled()
        }
    }

    val entryProvider = remember(navigator) {
        entryProvider {
            animeEntry(navigator)
            mangaEntry(navigator)
            seasonalEntry(navigator)
            bookmarkEntry(navigator)
            searchEntry(navigator)
            mediaDetailsEntry(navigator)
            onBoardingEntry(onFinished = navigator::goBack)
        }
    }

    Scaffold(
        bottomBar = {
            if (isOnBoardingChecked && navigationState.currentRoute in dashboardTopLevelRoutes) {
                NavigationBarComponent(
                    selectedRoute = navigationState.topLevelRoute,
                    onItemClick = navigator::navigate
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        if (isOnBoardingChecked) {
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
