package dev.fizcode.anime.domain.repository

import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.common.base.callhandler.DomainNetworkState

internal interface AnimeRepository {

    suspend fun fetchSeasonAnime(
        year: Int,
        season: String,
        sortBy: String,
        limit: Int
    ): DomainNetworkState<SeasonalAnimeDomainModel>

    suspend fun fetchTopAiringAnime(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<TopAiringDomainModel>

    suspend fun fetchTopRankingAnime(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<TopRankingDomainModel>

}
