package dev.fizcode.seasonal.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.processResponse
import dev.fizcode.mal.service.MalAnimeService
import dev.fizcode.seasonal.data.mapper.SeasonalDomainMapper
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel
import dev.fizcode.seasonal.domain.repository.SeasonalRepository
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.allows
import dev.fizcode.common.settings.current
import dev.fizcode.common.settings.includesNsfw

internal class SeasonalRepositoryImpl(
    private val animeService: MalAnimeService,
    private val seasonalDomainMapper: SeasonalDomainMapper,
    private val nsfwFilterProvider: NsfwFilterProvider
) : SeasonalRepository {

    override suspend fun fetchSeasonalAnime(
        year: Int,
        season: String,
        sortBy: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<SeasonalDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.fetchSeason(
            year = year,
            season = season,
            sortBy = sortBy,
            limit = limit,
            fields = fields,
            nsfw = nsfwFilter.includesNsfw
        )
        seasonalDomainMapper.mapToSeasonalAnime(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }
}
