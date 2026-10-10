package dev.fizcode.seasonal.domain.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel

internal interface SeasonalRepository {

    suspend fun fetchSeasonalAnime(
        year: Int,
        season: String,
        sortBy: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<SeasonalDomainModel>

}
