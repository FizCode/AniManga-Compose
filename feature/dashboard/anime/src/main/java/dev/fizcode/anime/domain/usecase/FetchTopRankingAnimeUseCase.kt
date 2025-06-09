package dev.fizcode.anime.domain.usecase

import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.common.util.AnimeFieldsConstant
import dev.fizcode.common.util.RankingTypeConstant
import dev.fizcode.common.util.extensions.fieldsPicker

internal class FetchTopRankingAnimeUseCase(
    private val animeRepository: AnimeRepository,
) {
    suspend operator fun invoke(limit: Int): UiState<TopRankingDomainModel> {
        val fields = fieldsPicker(
            AnimeFieldsConstant.MEDIA_TYPE,
            AnimeFieldsConstant.MEAN,
            AnimeFieldsConstant.BROADCAST,
            AnimeFieldsConstant.STATUS,
            AnimeFieldsConstant.MEDIA_TYPE,
            AnimeFieldsConstant.NUM_EPISODES,
            AnimeFieldsConstant.STUDIOS,
            AnimeFieldsConstant.GENRES
        )

        return animeRepository.fetchTopRankingAnime(
            rankingType = RankingTypeConstant.ALL,
            limit = limit,
            fields = fields
        )
    }
}
