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

internal class MediaDetailsRepositoryImpl(
    private val animeDetailsService: MediaDetailsService,
    private val animeDetailsDomainMapper: AnimeDetailsDomainMapper
) : MediaDetailsRepository {

    override suspend fun fetchMalAnimeDetails(
        animeId: Int,
        fields: String
    ): UiState<MalAnimeDetailsDomainModel> = processResponse {
        animeDetailsDomainMapper.mapToMalAnimeDetailsModel(
            animeDetailsService.fetchMalAnimeDetail(animeId = animeId, fields = fields)
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
