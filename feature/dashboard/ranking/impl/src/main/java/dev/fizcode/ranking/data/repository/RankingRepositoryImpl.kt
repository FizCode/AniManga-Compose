package dev.fizcode.ranking.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.processResponse
import dev.fizcode.datasource.remote.service.DashboardAnimeService
import dev.fizcode.ranking.data.mapper.RankingDomainMapper
import dev.fizcode.ranking.domain.model.RankingDomainModel
import dev.fizcode.ranking.domain.repository.RankingRepository

internal class RankingRepositoryImpl(
    private val animeService: DashboardAnimeService,
    private val rankingDomainMapper: RankingDomainMapper
) : RankingRepository {

    override suspend fun fetchAnimeRanking(
        rankingType: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<RankingDomainModel> = processResponse {
        rankingDomainMapper.mapToRanking(
            animeService.fetchTopRankingAnime(
                rankingType = rankingType,
                limit = limit,
                fields = fields
            )
        )
    }
}
