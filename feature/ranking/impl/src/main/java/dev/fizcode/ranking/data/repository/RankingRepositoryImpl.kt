package dev.fizcode.ranking.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.network.processResponse
import dev.fizcode.mal.service.MalAnimeService
import dev.fizcode.ranking.data.mapper.RankingDomainMapper
import dev.fizcode.ranking.domain.model.RankingDomainModel
import dev.fizcode.ranking.domain.repository.RankingRepository
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.allows
import dev.fizcode.common.settings.current
import dev.fizcode.common.settings.includesNsfw

internal class RankingRepositoryImpl(
    private val animeService: MalAnimeService,
    private val rankingDomainMapper: RankingDomainMapper,
    private val nsfwFilterProvider: NsfwFilterProvider
) : RankingRepository {

    override suspend fun fetchAnimeRanking(
        rankingType: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<RankingDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.fetchRanking(
            rankingType = rankingType,
            limit = limit,
            fields = fields,
            nsfw = nsfwFilter.includesNsfw
        )
        rankingDomainMapper.mapToRanking(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }
}
