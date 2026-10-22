package dev.fizcode.anime.data.repository

import dev.fizcode.anime.data.mapper.SeasonalDomainMapper
import dev.fizcode.anime.data.mapper.TopAiringDomainMapper
import dev.fizcode.anime.data.mapper.TopRankingDomainMapper
import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.network.processResponse
import dev.fizcode.mal.service.MalAnimeService
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.allows
import dev.fizcode.common.settings.current
import dev.fizcode.common.settings.includesNsfw

internal class AnimeRepositoryImpl(
    private val animeService: MalAnimeService,
    private val seasonalDomainMapper: SeasonalDomainMapper,
    private val topAiringDomainMapper: TopAiringDomainMapper,
    private val topRankingDomainMapper: TopRankingDomainMapper,
    private val nsfwFilterProvider: NsfwFilterProvider
) : AnimeRepository {

    override suspend fun fetchSeasonAnime(
        year: Int,
        season: String,
        sortBy: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<SeasonalAnimeDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.fetchSeason(
            year = year,
            season = season,
            sortBy = sortBy,
            limit = limit,
            fields = fields,
            nsfw = nsfwFilter.includesNsfw
        )
        seasonalDomainMapper.mapToSeasonAnime(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }

    override suspend fun fetchTopAiringAnime(
        rankingType: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<TopAiringDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.fetchRanking(
            rankingType = rankingType,
            limit = limit,
            fields = fields,
            nsfw = nsfwFilter.includesNsfw
        )
        topAiringDomainMapper.mapToTopAiringAnime(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }

    override suspend fun fetchTopRankingAnime(
        rankingType: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<TopRankingDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.fetchRanking(
            rankingType = rankingType,
            limit = limit,
            fields = fields,
            nsfw = nsfwFilter.includesNsfw
        )
        topRankingDomainMapper.mapToTopRankingAnime(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }
}
