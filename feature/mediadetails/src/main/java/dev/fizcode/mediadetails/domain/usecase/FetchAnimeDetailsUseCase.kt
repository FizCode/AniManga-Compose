package dev.fizcode.mediadetails.domain.usecase

import dev.fizcode.common.base.domainhandler.combineMalAndJikan
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.common.util.AnimeFieldsConstant
import dev.fizcode.common.util.extensions.fieldsPicker
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository

internal class FetchAnimeDetailsUseCase(
    private val mediaDetailsRepository: MediaDetailsRepository
) {
    suspend operator fun invoke(animeId: Int): UiState<AnimeDetailsDomainModel> {
        val fields = fieldsPicker(
            AnimeFieldsConstant.PICTURES,
            AnimeFieldsConstant.MEDIA_TYPE,
            AnimeFieldsConstant.STATUS,
            AnimeFieldsConstant.START_SEASON,
            AnimeFieldsConstant.NUM_EPISODES,
            AnimeFieldsConstant.AVERAGE_EPISODE_DURATION,
            AnimeFieldsConstant.STUDIOS,
            AnimeFieldsConstant.RANK,
            AnimeFieldsConstant.POPULARITY,
            AnimeFieldsConstant.NUM_LIST_USERS,
            AnimeFieldsConstant.MEAN,
            AnimeFieldsConstant.NUM_SCORING_USERS,
            AnimeFieldsConstant.GENRES,
            AnimeFieldsConstant.ALTERNATIVE_TITLES,
            AnimeFieldsConstant.SYNOPSIS,
            AnimeFieldsConstant.BACKGROUND,
            AnimeFieldsConstant.START_DATE,
            AnimeFieldsConstant.END_DATE,
            AnimeFieldsConstant.BROADCAST,
            AnimeFieldsConstant.SOURCE,
            AnimeFieldsConstant.RATING,
            AnimeFieldsConstant.RELATED_ANIME
        )
        val malRequest =
            mediaDetailsRepository.fetchMalAnimeDetails(animeId = animeId, fields = fields)
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
