package dev.fizcode.anime.domain.repository

import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.common.base.responsehandler.UiState

internal interface AnimeRepository {

    suspend fun fetchSeasonAnime(): UiState<SeasonalAnimeDomainModel>

    suspend fun fetchTopAiringAnime(): UiState<TopAiringDomainModel>

    suspend fun fetchTopRankingAnime(): UiState<TopRankingDomainModel>

}
