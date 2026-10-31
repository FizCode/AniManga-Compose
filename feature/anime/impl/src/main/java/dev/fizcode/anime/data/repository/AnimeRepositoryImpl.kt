package dev.fizcode.anime.data.repository

import dev.fizcode.anime.data.mapper.SeasonalDomainMapper
import dev.fizcode.anime.data.mapper.TopAiringDomainMapper
import dev.fizcode.anime.data.mapper.TopRankingDomainMapper
import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mal.util.AnimeFieldsConstant
import dev.fizcode.mal.util.fieldsPicker
import dev.fizcode.network.processResponse
import dev.fizcode.mal.service.MalAnimeService
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.allows
import dev.fizcode.common.settings.current
import dev.fizcode.common.settings.includesNsfw

private val SEASON_FIELDS = fieldsPicker(
    AnimeFieldsConstant.ALTERNATIVE_TITLES,
    AnimeFieldsConstant.NSFW,
    AnimeFieldsConstant.MEDIA_TYPE,
    AnimeFieldsConstant.MEAN,
    AnimeFieldsConstant.BROADCAST,
    AnimeFieldsConstant.POPULARITY,
    AnimeFieldsConstant.STATUS,
    AnimeFieldsConstant.NUM_EPISODES,
    AnimeFieldsConstant.STUDIOS,
    AnimeFieldsConstant.SYNOPSIS,
    AnimeFieldsConstant.GENRES
)

private val TOP_AIRING_FIELDS = fieldsPicker(
    AnimeFieldsConstant.ALTERNATIVE_TITLES,
    AnimeFieldsConstant.NSFW,
    AnimeFieldsConstant.MEDIA_TYPE,
    AnimeFieldsConstant.MEAN
)

private val TOP_RANKING_FIELDS = fieldsPicker(
    AnimeFieldsConstant.ALTERNATIVE_TITLES,
    AnimeFieldsConstant.NSFW,
    AnimeFieldsConstant.MEDIA_TYPE,
    AnimeFieldsConstant.MEAN,
    AnimeFieldsConstant.BROADCAST,
    AnimeFieldsConstant.STATUS,
    AnimeFieldsConstant.MEDIA_TYPE,
    AnimeFieldsConstant.NUM_EPISODES,
    AnimeFieldsConstant.STUDIOS,
    AnimeFieldsConstant.GENRES
)

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
        limit: Int
    ): DomainNetworkState<SeasonalAnimeDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.fetchSeason(
            year = year,
            season = season,
            sortBy = sortBy,
            limit = limit,
            fields = SEASON_FIELDS,
            nsfw = nsfwFilter.includesNsfw
        )
        seasonalDomainMapper.mapToSeasonAnime(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }

    override suspend fun fetchTopAiringAnime(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<TopAiringDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.fetchRanking(
            rankingType = rankingType,
            limit = limit,
            fields = TOP_AIRING_FIELDS,
            nsfw = nsfwFilter.includesNsfw
        )
        topAiringDomainMapper.mapToTopAiringAnime(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }

    override suspend fun fetchTopRankingAnime(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<TopRankingDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.fetchRanking(
            rankingType = rankingType,
            limit = limit,
            fields = TOP_RANKING_FIELDS,
            nsfw = nsfwFilter.includesNsfw
        )
        topRankingDomainMapper.mapToTopRankingAnime(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }
}
