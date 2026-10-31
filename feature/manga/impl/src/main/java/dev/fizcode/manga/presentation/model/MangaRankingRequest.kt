package dev.fizcode.manga.presentation.model

import dev.fizcode.manga.util.Constant

internal data class MangaRankingRequest(val rankingType: String, val limit: Int) {

    companion object {
        fun of(type: MangaType, sort: MangaSort) = MangaRankingRequest(
            rankingType = sort.rankingType ?: type.rankingType,
            limit = if (sort.rankingType == null) type.limit else Constant.RANKING_LIMIT
        )
    }
}
