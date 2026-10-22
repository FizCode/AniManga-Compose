package dev.fizcode.ranking.presentation.model

import dev.fizcode.mal.util.RankingTypeConstant

/** Which ranking a screen lists: its title and the MAL `ranking_type` it asks for. */
internal enum class RankingType(val title: String, val apiValue: String) {
    TOP_AIRING("Top Airing ✨", RankingTypeConstant.AIRING),
    TOP_RANKING("Top Ranking 🏆", RankingTypeConstant.ALL)
}
