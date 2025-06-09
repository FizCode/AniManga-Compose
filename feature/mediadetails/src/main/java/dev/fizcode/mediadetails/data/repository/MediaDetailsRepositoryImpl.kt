package dev.fizcode.mediadetails.data.repository

import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.common.base.responsehandler.processResponse
import dev.fizcode.mediadetails.data.mapper.AnimeDetailsDomainMapper
import dev.fizcode.mediadetails.data.service.MediaDetailsService
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
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
            detailsResponse = animeDetailsService.fetchJikanAnimeDetail(animeId = animeId)
        )
    }

    override suspend fun fetchAnimeCast(
        animeId: Int
    ): UiState<List<JikanCastDomainModel>> = processResponse {
        animeDetailsDomainMapper.mapToCastDomainModel(
            cast = animeDetailsService.fetchJikanVoiceActors(animeId = animeId)
        )
    }

    override suspend fun fetchAnimeStaff(
        animeId: Int
    ): UiState<List<JikanStaffDomainModel>> = processResponse {
        animeDetailsDomainMapper.mapToStaffDomainModel(
            staff = animeDetailsService.fetchJikanStaff(animeId = animeId)
        )
    }

}
