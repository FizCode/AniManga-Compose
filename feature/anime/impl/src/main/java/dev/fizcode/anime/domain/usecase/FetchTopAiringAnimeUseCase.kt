package dev.fizcode.anime.domain.usecase

import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mal.util.AnimeFieldsConstant
import dev.fizcode.mal.util.RankingTypeConstant
import dev.fizcode.mal.util.fieldsPicker

internal class FetchTopAiringAnimeUseCase(
    private val animeRepository: AnimeRepository
) {

    suspend operator fun invoke(limit: Int): DomainNetworkState<TopAiringDomainModel> {
        val fields = fieldsPicker(
            AnimeFieldsConstant.ALTERNATIVE_TITLES,
            AnimeFieldsConstant.NSFW,
            AnimeFieldsConstant.MEDIA_TYPE,
            AnimeFieldsConstant.MEAN
        )

        return animeRepository.fetchTopAiringAnime(
            rankingType = RankingTypeConstant.AIRING,
            limit = limit,
            fields = fields
        )
    }
}