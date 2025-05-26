package dev.fizcode.mediadetails.data.repository

import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.common.base.responsehandler.processResponse
import dev.fizcode.mediadetails.data.mapper.AnimeDetailsDomainMapper
import dev.fizcode.mediadetails.data.service.MediaDetailsService
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import javax.inject.Inject

internal class MediaDetailsRepositoryImpl @Inject constructor(
    private val animeDetailsService: MediaDetailsService,
    private val animeDetailsDomainMapper: AnimeDetailsDomainMapper
) : MediaDetailsRepository {

    override suspend fun fetchMalAnimeDetails(
        animeId: Int
    ): UiState<MalAnimeDetailsDomainModel> = processResponse {
        animeDetailsDomainMapper.mapToMalAnimeDetailsModel(
            animeDetailsService.fetchMalAnimeDetail(animeId = animeId)
        )
    }

    override suspend fun fetchJikanAnimeDetails(
        animeId: Int
    ): UiState<JikanAnimeDetailsDomainModel> = processResponse {
        animeDetailsDomainMapper.mapToJikanAnimeDetailsModel(
            detailsResponse = animeDetailsService.fetchJikanAnimeDetail(animeId = animeId),
            castResponse = animeDetailsService.fetchJikanVoiceActors(animeId = animeId),
            staffResponse = animeDetailsService.fetchJikanStaff(animeId = animeId)
        )
    }

}
