package dev.fizcode.search.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.processResponse
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.allows
import dev.fizcode.common.settings.current
import dev.fizcode.common.settings.includesNsfw
import dev.fizcode.mal.service.MalAnimeService
import dev.fizcode.search.data.mapper.SearchDomainMapper
import dev.fizcode.search.domain.model.SearchAnimeDomainModel
import dev.fizcode.search.domain.repository.SearchRepository

internal class SearchRepositoryImpl(
    private val animeService: MalAnimeService,
    private val searchDomainMapper: SearchDomainMapper,
    private val nsfwFilterProvider: NsfwFilterProvider
) : SearchRepository {

    override suspend fun searchAnime(
        query: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<SearchAnimeDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = animeService.search(
            query = query,
            limit = limit,
            fields = fields,
            nsfw = nsfwFilter.includesNsfw
        )
        searchDomainMapper.mapToSearchAnime(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }
}
