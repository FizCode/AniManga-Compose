package dev.fizcode.manga.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.manga.domain.model.MangaDomainModel
import dev.fizcode.manga.domain.repository.MangaRepository

internal class FetchMangaRankingUseCase(
    private val mangaRepository: MangaRepository
) {
    suspend operator fun invoke(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<MangaDomainModel> {
        return mangaRepository.fetchMangaRanking(
            rankingType = rankingType,
            limit = limit
        )
    }
}
