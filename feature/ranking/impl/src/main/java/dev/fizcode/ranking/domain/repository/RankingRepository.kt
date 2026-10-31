package dev.fizcode.ranking.domain.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.ranking.domain.model.RankingDomainModel

internal interface RankingRepository {

    suspend fun fetchAnimeRanking(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<RankingDomainModel>

}
