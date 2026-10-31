package dev.fizcode.mediadetails.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository

internal class FetchAnimeDetailsUseCase(
    private val mediaDetailsRepository: MediaDetailsRepository
) {
    suspend operator fun invoke(animeId: Int): DomainNetworkState<AnimeDetailsDomainModel> {
        return mediaDetailsRepository.fetchAnimeDetails(animeId = animeId)
    }
}
