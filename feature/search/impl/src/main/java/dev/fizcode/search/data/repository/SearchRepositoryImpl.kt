package dev.fizcode.search.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.processResponse
import dev.fizcode.datasource.remote.service.DashboardAnimeService
import dev.fizcode.search.data.mapper.SearchDomainMapper
import dev.fizcode.search.domain.model.SearchAnimeDomainModel
import dev.fizcode.search.domain.repository.SearchRepository

internal class SearchRepositoryImpl(
    private val animeService: DashboardAnimeService,
    private val searchDomainMapper: SearchDomainMapper
) : SearchRepository {

    override suspend fun searchAnime(
        query: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<SearchAnimeDomainModel> = processResponse {
        searchDomainMapper.mapToSearchAnime(
            animeService.searchAnime(
                query = query,
                limit = limit,
                fields = fields
            )
        )
    }
}
