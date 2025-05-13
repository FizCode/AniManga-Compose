package dev.fizcode.anime.domain.usecase

import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.common.util.AnimeFieldsConstant
import dev.fizcode.common.util.AnimeSortingConstant
import dev.fizcode.common.util.SeasonHelper
import dev.fizcode.common.util.fieldsPicker

internal class FetchSeasonalAnimeUseCase(
    private val animeRepository: AnimeRepository,
    private val seasonHelper: SeasonHelper
) {
    suspend operator fun invoke(limit: Int): UiState<SeasonalAnimeDomainModel> {
        val currentYear = seasonHelper.getCurrentYear()
        val currentSeason = seasonHelper.getCurrentSeason()
        val fields = fieldsPicker(
            AnimeFieldsConstant.MEDIA_TYPE,
            AnimeFieldsConstant.MEAN,
            AnimeFieldsConstant.BROADCAST,
            AnimeFieldsConstant.POPULARITY,
            AnimeFieldsConstant.STATUS,
            AnimeFieldsConstant.NUM_EPISODES,
            AnimeFieldsConstant.STUDIOS,
            AnimeFieldsConstant.SYNOPSIS,
            AnimeFieldsConstant.GENRES
        )

        return animeRepository.fetchSeasonAnime(
            year = currentYear,
            season = currentSeason,
            sortBy = AnimeSortingConstant.ANIME_SCORE,
            limit = limit,
            fields = fields
        )
    }
}
