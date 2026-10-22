package dev.fizcode.manga.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.network.processResponse
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.allows
import dev.fizcode.common.settings.current
import dev.fizcode.common.settings.includesNsfw
import dev.fizcode.mal.service.MalMangaService
import dev.fizcode.manga.data.mapper.MangaDomainMapper
import dev.fizcode.manga.domain.model.MangaDomainModel
import dev.fizcode.manga.domain.repository.MangaRepository

internal class MangaRepositoryImpl(
    private val mangaService: MalMangaService,
    private val mangaDomainMapper: MangaDomainMapper,
    private val nsfwFilterProvider: NsfwFilterProvider
) : MangaRepository {

    override suspend fun fetchMangaRanking(
        rankingType: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<MangaDomainModel> = processResponse {
        val nsfwFilter = nsfwFilterProvider.current()
        val response = mangaService.fetchRanking(
            rankingType = rankingType,
            limit = limit,
            fields = fields,
            nsfw = nsfwFilter.includesNsfw
        )
        mangaDomainMapper.mapToManga(
            response.copy(data = response.data?.filter { nsfwFilter.allows(it.node?.nsfw) })
        )
    }
}
