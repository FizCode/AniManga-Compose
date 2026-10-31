package dev.fizcode.seasonal.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mal.util.AnimeSortingConstant
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel
import dev.fizcode.seasonal.domain.repository.SeasonalRepository

internal class FetchSeasonalAnimeUseCase(
    private val seasonalRepository: SeasonalRepository
) {
    suspend operator fun invoke(
        year: Int,
        season: String,
        limit: Int
    ): DomainNetworkState<SeasonalDomainModel> {
        return seasonalRepository.fetchSeasonalAnime(
            year = year,
            season = season,
            sortBy = AnimeSortingConstant.ANIME_SCORE,
            limit = limit
        )
    }
}
