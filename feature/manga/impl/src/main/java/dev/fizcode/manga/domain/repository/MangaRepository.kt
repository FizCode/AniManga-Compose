package dev.fizcode.manga.domain.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.manga.domain.model.MangaDomainModel

internal interface MangaRepository {

    suspend fun fetchMangaRanking(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<MangaDomainModel>

}
