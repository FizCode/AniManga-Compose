package dev.fizcode.search.domain.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.search.domain.model.SearchAnimeDomainModel

internal interface SearchRepository {

    suspend fun searchAnime(
        query: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<SearchAnimeDomainModel>

}
