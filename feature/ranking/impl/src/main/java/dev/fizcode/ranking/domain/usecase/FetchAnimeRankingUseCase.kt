package dev.fizcode.ranking.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.ranking.domain.model.RankingDomainModel
import dev.fizcode.ranking.domain.repository.RankingRepository

internal class FetchAnimeRankingUseCase(
    private val rankingRepository: RankingRepository
) {
    suspend operator fun invoke(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<RankingDomainModel> {
        return rankingRepository.fetchAnimeRanking(
            rankingType = rankingType,
            limit = limit
        )
    }
}
