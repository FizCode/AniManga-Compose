package dev.fizcode.mediadetails.domain.repository

import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel

internal interface MediaDetailsRepository {

    suspend fun fetchMalAnimeDetails(
        animeId: Int,
        fields: String
    ): UiState<MalAnimeDetailsDomainModel>

    suspend fun fetchJikanAnimeDetails(animeId: Int): UiState<JikanAnimeDetailsDomainModel>

    suspend fun fetchAnimeCast(animeId: Int): UiState<List<JikanCastDomainModel>>

    suspend fun fetchAnimeStaff(animeId: Int): UiState<List<JikanStaffDomainModel>>

}
