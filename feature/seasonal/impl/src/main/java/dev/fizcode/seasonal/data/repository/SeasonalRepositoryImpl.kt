package dev.fizcode.seasonal.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mal.util.AnimeFieldsConstant
import dev.fizcode.mal.util.fieldsPicker
import dev.fizcode.network.processResponse
import dev.fizcode.mal.service.MalAnimeService
import dev.fizcode.seasonal.data.mapper.SeasonalDomainMapper
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel
import dev.fizcode.seasonal.domain.repository.SeasonalRepository
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.allows
import dev.fizcode.common.settings.current
import dev.fizcode.common.settings.includesNsfw

private val SEASONAL_FIELDS = fieldsPicker(
    AnimeFieldsConstant.ALTERNATIVE_TITLES,
    AnimeFieldsConstant.NSFW,
    AnimeFieldsConstant.MEDIA_TYPE,
    AnimeFieldsConstant.MEAN,
    AnimeFieldsConstant.BROADCAST,
    AnimeFieldsConstant.STATUS,
    AnimeFieldsConstant.NUM_EPISODES,
    AnimeFieldsConstant.STUDIOS,
    AnimeFieldsConstant.SYNOPSIS,
    AnimeFieldsConstant.GENRES
)

internal class SeasonalRepositoryImpl(
    private val animeService: MalAnimeService,
    private val seasonalDomainMapper: SeasonalDomainMapper,
    private val nsfwFilterProvider: NsfwFilterProvider
) : SeasonalRepository {

    override suspend fun fetchSeasonalAnime(
        year: Int,
        season: String,
        sortBy: String,
        limit: Int
    ): DomainNetworkState<SeasonalDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.fetchSeason(
            year = year,
            season = season,
            sortBy = sortBy,
            limit = limit,
            fields = SEASONAL_FIELDS,
            nsfw = nsfwFilter.includesNsfw
        )
        seasonalDomainMapper.mapToSeasonalAnime(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }
}
