package dev.fizcode.ranking.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.util.AnimeFieldsConstant
import dev.fizcode.common.util.extensions.fieldsPicker
import dev.fizcode.ranking.domain.model.RankingDomainModel
import dev.fizcode.ranking.domain.repository.RankingRepository

internal class FetchAnimeRankingUseCase(
    private val rankingRepository: RankingRepository
) {
    suspend operator fun invoke(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<RankingDomainModel> {
        val fields = fieldsPicker(
            AnimeFieldsConstant.MEDIA_TYPE,
            AnimeFieldsConstant.MEAN,
            AnimeFieldsConstant.BROADCAST,
            AnimeFieldsConstant.STATUS,
            AnimeFieldsConstant.NUM_EPISODES,
            AnimeFieldsConstant.STUDIOS,
            AnimeFieldsConstant.SYNOPSIS,
            AnimeFieldsConstant.GENRES
        )

        return rankingRepository.fetchAnimeRanking(
            rankingType = rankingType,
            limit = limit,
            fields = fields
        )
    }
}
