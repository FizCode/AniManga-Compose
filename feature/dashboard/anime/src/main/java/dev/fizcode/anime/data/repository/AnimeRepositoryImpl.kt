package dev.fizcode.anime.data.repository

import dev.fizcode.anime.data.mapper.SeasonalDomainMapper
import dev.fizcode.anime.data.mapper.TopAiringDomainMapper
import dev.fizcode.anime.data.mapper.TopRankingDomainMapper
import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.common.base.responsehandler.processResponse
import dev.fizcode.datasource.remote.service.DashboardAnimeService
import javax.inject.Inject

internal class AnimeRepositoryImpl @Inject constructor(
    private val animeService: DashboardAnimeService,
    private val seasonalDomainMapper: SeasonalDomainMapper,
    private val topAiringDomainMapper: TopAiringDomainMapper,
    private val topRankingDomainMapper: TopRankingDomainMapper
) : AnimeRepository {

    override suspend fun fetchSeasonAnime(): UiState<SeasonalAnimeDomainModel> =
        processResponse {
            seasonalDomainMapper.mapToSeasonAnime(animeService.fetchSeasonAnime())
        }

    override suspend fun fetchTopAiringAnime(): UiState<TopAiringDomainModel> =
        processResponse {
            topAiringDomainMapper.mapToTopAiringAnime(animeService.fetchTopAiringAnime())
        }

    override suspend fun fetchTopRankingAnime(): UiState<TopRankingDomainModel> =
        processResponse {
            topRankingDomainMapper.mapToTopRankingAnime(animeService.fetchTopRankingAnime())
        }
}
