package dev.fizcode.mediadetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import dev.fizcode.common.util.DEEPLINK_BASE
import dev.fizcode.mediadetails.presentation.MediaDetailsScreen
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun NavGraphBuilder.mediaDetailsNavGraph() {
    composable<MediaDetailsRoute>(
        deepLinks = listOf(
            navDeepLink<MediaDetailsRoute>(basePath = "$DEEPLINK_BASE/details")
        )
    ) { backstackEntry ->
        val backstackEntryRoute = backstackEntry.toRoute<MediaDetailsRoute>()
        MediaDetailsScreen(
            mediaType = backstackEntryRoute.mediaType,
            mediaId = backstackEntryRoute.mediaId
        )
    }
}

fun NavController.navigateToMediaDetailsScreen(
    mediaType: String,
    mediaId: Int,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = MediaDetailsRoute(mediaType = mediaType, mediaId = mediaId)) {
    navOptions()
}

@Serializable
data class MediaDetailsRoute(
    @SerialName("mediaType")
    val mediaType: String,
    val mediaId: Int
)