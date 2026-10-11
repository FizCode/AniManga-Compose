package dev.fizcode.manga.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.processResponse
import dev.fizcode.datasource.remote.service.MangaService
import dev.fizcode.manga.data.mapper.MangaDomainMapper
import dev.fizcode.manga.domain.model.MangaDomainModel
import dev.fizcode.manga.domain.repository.MangaRepository

internal class MangaRepositoryImpl(
    private val mangaService: MangaService,
    private val mangaDomainMapper: MangaDomainMapper
) : MangaRepository {

    override suspend fun fetchMangaRanking(
        rankingType: String,
        limit: Int,
        fields: String
    ): DomainNetworkState<MangaDomainModel> = processResponse {
        mangaDomainMapper.mapToManga(
            mangaService.fetchMangaRanking(
                rankingType = rankingType,
                limit = limit,
                fields = fields
            )
        )
    }
}
