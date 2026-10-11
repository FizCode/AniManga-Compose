package dev.fizcode.anime.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.fizcode.anime.api.AnimeRoute
import dev.fizcode.anime.presentation.AnimeScreen
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.navigation.Navigator
import dev.fizcode.navigation.topLevelEntryMetadata
import dev.fizcode.ranking.api.TopAiringRoute
import dev.fizcode.ranking.api.TopRankingRoute
import dev.fizcode.search.api.SearchRoute
import dev.fizcode.settings.api.SettingsRoute
import dev.fizcode.seasonal.api.SeasonalRoute

fun EntryProviderScope<NavKey>.animeEntry(navigator: Navigator) {
    entry<AnimeRoute>(metadata = topLevelEntryMetadata()) {
        AnimeScreen(
            onCardClick = { mediaType, mediaId ->
                navigator.navigate(MediaDetailsRoute(mediaType = mediaType, mediaId = mediaId))
            },
            onSearchClick = { navigator.navigate(SearchRoute) },
            onCurrentSeasonClick = { navigator.navigate(SeasonalRoute) },
            onTopAiringClick = { navigator.navigate(TopAiringRoute) },
            onTopRankingClick = { navigator.navigate(TopRankingRoute) },
            onSettingsClick = { navigator.navigate(SettingsRoute) }
        )
    }
}
