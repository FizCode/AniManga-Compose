package dev.fizcode.seasonal.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.processResponse
import dev.fizcode.datasource.remote.service.DashboardAnimeService
import dev.fizcode.seasonal.data.mapper.SeasonalDomainMapper
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel
import dev.fizcode.seasonal.domain.repository.SeasonalRepository

internal class SeasonalRepositoryImpl(
    private val animeService: DashboardAnimeService,
    private val seasonalDomainMapper: SeasonalDomainMapper
) : SeasonalRepository {

    override suspend fun fetchSeasonalAnime(
        year: Int,
        season: String,
        sortBy: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<SeasonalDomainModel> = processResponse {
        seasonalDomainMapper.mapToSeasonalAnime(
            animeService.fetchSeasonAnime(
                year = year,
                season = season,
                sortBy = sortBy,
                limit = limit,
                fields = fields
            )
        )
    }
}
