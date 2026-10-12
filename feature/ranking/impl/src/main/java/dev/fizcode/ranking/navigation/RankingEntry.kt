package dev.fizcode.ranking.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.navigation.Navigator
import dev.fizcode.ranking.api.TopAiringRoute
import dev.fizcode.ranking.api.TopRankingRoute
import dev.fizcode.ranking.presentation.RankingScreen
import dev.fizcode.ranking.presentation.model.RankingType

fun EntryProviderScope<NavKey>.rankingEntry(navigator: Navigator) {
    entry<TopAiringRoute> {
        RankingScreen(
            type = RankingType.TOP_AIRING,
            onBackClick = navigator::goBack,
            onCardClick = { mediaType, mediaId ->
                navigator.navigate(MediaDetailsRoute(mediaType = mediaType, mediaId = mediaId))
            }
        )
    }
    entry<TopRankingRoute> {
        RankingScreen(
            type = RankingType.TOP_RANKING,
            onBackClick = navigator::goBack,
            onCardClick = { mediaType, mediaId ->
                navigator.navigate(MediaDetailsRoute(mediaType = mediaType, mediaId = mediaId))
            }
        )
    }
}
