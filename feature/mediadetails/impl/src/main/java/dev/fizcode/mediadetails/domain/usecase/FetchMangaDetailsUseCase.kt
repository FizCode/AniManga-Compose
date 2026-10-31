package dev.fizcode.mediadetails.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.domain.model.MangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MangaDetailsRepository

internal class FetchMangaDetailsUseCase(
    private val mangaDetailsRepository: MangaDetailsRepository
) {
    suspend operator fun invoke(mangaId: Int): DomainNetworkState<MangaDetailsDomainModel> {
        return mangaDetailsRepository.fetchMangaDetails(mangaId = mangaId)
    }
}
