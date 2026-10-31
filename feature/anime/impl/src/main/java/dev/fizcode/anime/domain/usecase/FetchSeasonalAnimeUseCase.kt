package dev.fizcode.anime.domain.usecase

import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mal.util.AnimeSortingConstant
import dev.fizcode.mal.util.SeasonHelper

internal class FetchSeasonalAnimeUseCase(
    private val animeRepository: AnimeRepository,
    private val seasonHelper: SeasonHelper
) {
    suspend operator fun invoke(limit: Int): DomainNetworkState<SeasonalAnimeDomainModel> {
        val currentYear = seasonHelper.getCurrentYear()
        val currentSeason = seasonHelper.getCurrentSeason().apiValue

        return animeRepository.fetchSeasonAnime(
            year = currentYear,
            season = currentSeason,
            sortBy = AnimeSortingConstant.ANIME_SCORE,
            limit = limit
        )
    }
}
