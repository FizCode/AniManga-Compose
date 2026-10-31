package dev.fizcode.anime.domain.usecase

import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mal.util.RankingTypeConstant

internal class FetchTopAiringAnimeUseCase(
    private val animeRepository: AnimeRepository
) {

    suspend operator fun invoke(limit: Int): DomainNetworkState<TopAiringDomainModel> {
        return animeRepository.fetchTopAiringAnime(
            rankingType = RankingTypeConstant.AIRING,
            limit = limit
        )
    }
}