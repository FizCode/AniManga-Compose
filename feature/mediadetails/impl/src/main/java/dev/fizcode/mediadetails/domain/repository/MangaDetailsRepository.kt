package dev.fizcode.mediadetails.domain.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.MangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.PagedResult

internal interface MangaDetailsRepository {

    suspend fun fetchMangaDetails(
        mangaId: Int,
        fields: String
    ): DomainNetworkState<MangaDetailsDomainModel>

    suspend fun fetchMangaCharacters(mangaId: Int): DomainNetworkState<List<CastDomainModel>>

    suspend fun fetchMangaCharactersPage(
        mangaId: Int,
        page: Int,
        perPage: Int
    ): DomainNetworkState<PagedResult<CastDomainModel>>
}
