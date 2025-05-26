package dev.fizcode.mediadetails.domain.repository

import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel

internal interface MediaDetailsRepository {

    suspend fun fetchMalAnimeDetails(animeId: Int): UiState<MalAnimeDetailsDomainModel>

    suspend fun fetchJikanAnimeDetails(animeId: Int): UiState<JikanAnimeDetailsDomainModel>

}
