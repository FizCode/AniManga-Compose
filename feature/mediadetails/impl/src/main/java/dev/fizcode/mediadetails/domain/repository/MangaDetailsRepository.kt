package dev.fizcode.mediadetails.domain.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.MangaDetailsDomainModel

internal interface MangaDetailsRepository {

    suspend fun fetchMangaDetails(
        mangaId: Int,
        fields: String
    ): DomainNetworkState<MangaDetailsDomainModel>

    suspend fun fetchMangaCharacters(mangaId: Int): DomainNetworkState<List<CastDomainModel>>
}
