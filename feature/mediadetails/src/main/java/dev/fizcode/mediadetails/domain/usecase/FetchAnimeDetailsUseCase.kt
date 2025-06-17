package dev.fizcode.mediadetails.domain.usecase

import dev.fizcode.common.base.domainhandler.combineMalAndJikan
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository

internal class FetchAnimeDetailsUseCase(
    private val mediaDetailsRepository: MediaDetailsRepository
) {
    suspend operator fun invoke(animeId: Int): UiState<AnimeDetailsDomainModel> {
        val malRequest = mediaDetailsRepository.fetchMalAnimeDetails(animeId = animeId)
        val jikanRequest = mediaDetailsRepository.fetchJikanAnimeDetails(animeId = animeId)

        return combineMalAndJikan(
            domain1 = { malRequest },
            domain2 = { jikanRequest },
            returnModel = { mal, jikan ->
                AnimeDetailsDomainModel(
                    malDomainModel = mal,
                    jikanDomainModel = jikan
                )
            }
        )
    }
}
