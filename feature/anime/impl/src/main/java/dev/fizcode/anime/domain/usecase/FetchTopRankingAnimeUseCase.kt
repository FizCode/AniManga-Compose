package dev.fizcode.anime.domain.usecase

import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mal.util.RankingTypeConstant

internal class FetchTopRankingAnimeUseCase(
    private val animeRepository: AnimeRepository,
) {
    suspend operator fun invoke(limit: Int): DomainNetworkState<TopRankingDomainModel> {
        return animeRepository.fetchTopRankingAnime(
            rankingType = RankingTypeConstant.ALL,
            limit = limit
        )
    }
}
