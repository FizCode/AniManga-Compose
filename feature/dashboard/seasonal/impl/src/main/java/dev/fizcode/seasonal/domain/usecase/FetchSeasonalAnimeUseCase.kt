package dev.fizcode.seasonal.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.util.AnimeFieldsConstant
import dev.fizcode.common.util.AnimeSortingConstant
import dev.fizcode.common.util.extensions.fieldsPicker
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
        val fields = fieldsPicker(
            AnimeFieldsConstant.MEDIA_TYPE,
            AnimeFieldsConstant.MEAN,
            AnimeFieldsConstant.BROADCAST,
            AnimeFieldsConstant.STATUS,
            AnimeFieldsConstant.NUM_EPISODES,
            AnimeFieldsConstant.STUDIOS,
            AnimeFieldsConstant.SYNOPSIS,
            AnimeFieldsConstant.GENRES
        )

        return seasonalRepository.fetchSeasonalAnime(
            year = year,
            season = season,
            sortBy = AnimeSortingConstant.ANIME_SCORE,
            limit = limit,
            fields = fields
        )
    }
}
