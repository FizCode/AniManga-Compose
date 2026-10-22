package dev.fizcode.anime.domain.usecase

import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mal.util.AnimeFieldsConstant
import dev.fizcode.mal.util.AnimeSortingConstant
import dev.fizcode.mal.util.SeasonHelper
import dev.fizcode.mal.util.fieldsPicker

internal class FetchSeasonalAnimeUseCase(
    private val animeRepository: AnimeRepository,
    private val seasonHelper: SeasonHelper
) {
    suspend operator fun invoke(limit: Int): DomainNetworkState<SeasonalAnimeDomainModel> {
        val currentYear = seasonHelper.getCurrentYear()
        val currentSeason = seasonHelper.getCurrentSeason()
        val fields = fieldsPicker(
            AnimeFieldsConstant.ALTERNATIVE_TITLES,
            AnimeFieldsConstant.NSFW,
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
