package dev.fizcode.ranking.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/** Full list of the anime that are airing right now, best score first. */
@Serializable
data object TopAiringRoute : NavKey

/** Full list of the best ranked anime of all time. */
@Serializable
data object TopRankingRoute : NavKey
