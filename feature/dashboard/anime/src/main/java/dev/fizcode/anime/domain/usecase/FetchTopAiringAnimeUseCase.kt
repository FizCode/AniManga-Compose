package dev.fizcode.anime.domain.usecase

import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.common.util.AnimeFieldsConstant
import dev.fizcode.common.util.RankingTypeConstant
import dev.fizcode.common.util.fieldsPicker

internal class FetchTopAiringAnimeUseCase(
    private val animeRepository: AnimeRepository
) {

    suspend operator fun invoke(limit: Int): UiState<TopAiringDomainModel> {
        val fields = fieldsPicker(AnimeFieldsConstant.MEAN)

        return animeRepository.fetchTopAiringAnime(
            rankingType = RankingTypeConstant.AIRING,
            limit = limit,
            fields = fields
        )
    }
}