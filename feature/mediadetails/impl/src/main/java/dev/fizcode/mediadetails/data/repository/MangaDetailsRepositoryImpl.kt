package dev.fizcode.mediadetails.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.network.processResponse
import dev.fizcode.mediadetails.data.mapper.AniListDomainMapper
import dev.fizcode.mediadetails.data.mapper.MangaDetailsDomainMapper
import dev.fizcode.mediadetails.data.service.AniListService
import dev.fizcode.mediadetails.data.service.MediaDetailsService
import dev.fizcode.mediadetails.domain.model.AniListMangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.MalMangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.MangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MangaDetailsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import dev.fizcode.mediadetails.domain.model.PagedResult

internal class MangaDetailsRepositoryImpl(
    private val detailsService: MediaDetailsService,
    private val aniListService: AniListService,
    private val mangaDetailsDomainMapper: MangaDetailsDomainMapper,
    private val aniListDomainMapper: AniListDomainMapper
) : MangaDetailsRepository {

    override suspend fun fetchMangaDetails(
        mangaId: Int,
        fields: String
    ): DomainNetworkState<MangaDetailsDomainModel> = coroutineScope {
        val malRequest = async { fetchMalMangaDetails(mangaId = mangaId, fields = fields) }
        val aniListRequest = async { fetchAniListDetails(mangaId = mangaId) }

        mergeMalMangaWithOptionalAniList(mal = malRequest.await(), aniList = aniListRequest.await())
    }

    private suspend fun fetchMalMangaDetails(
        mangaId: Int,
        fields: String
    ): DomainNetworkState<MalMangaDetailsDomainModel> = processResponse {
        mangaDetailsDomainMapper.mapToMalMangaDetailsModel(
            detailsService.fetchMalMangaDetail(mangaId = mangaId, fields = fields)
        )
    }

    private suspend fun fetchAniListDetails(
        mangaId: Int
    ): DomainNetworkState<AniListMangaDetailsDomainModel> = processResponse {
        aniListDomainMapper.mapToMangaDetailsDomainModel(
            aniListService.fetchMangaDetails(malId = mangaId)
        )
    }

    override suspend fun fetchMangaCharacters(
        mangaId: Int
    ): DomainNetworkState<List<CastDomainModel>> = processResponse {
        aniListDomainMapper.mapToCastDomainModel(aniListService.fetchMangaCharacters(malId = mangaId))
    }

    override suspend fun fetchMangaCharactersPage(
        mangaId: Int,
        page: Int,
        perPage: Int
    ): DomainNetworkState<PagedResult<CastDomainModel>> = processResponse {
        aniListDomainMapper.mapToCastPage(
            aniListService.fetchMangaCharactersPage(malId = mangaId, page = page, perPage = perPage)
        )
    }
}

/** Manga counterpart of [mergeMalWithOptionalAniList]: AniList only adds favorites and themes. */
internal fun mergeMalMangaWithOptionalAniList(
    mal: DomainNetworkState<MalMangaDetailsDomainModel>,
    aniList: DomainNetworkState<AniListMangaDetailsDomainModel>
): DomainNetworkState<MangaDetailsDomainModel> = when (mal) {
    is DomainNetworkState.Success -> DomainNetworkState.Success(
        MangaDetailsDomainModel(
            malDomainModel = mal.data,
            aniListDomainModel = (aniList as? DomainNetworkState.Success)?.data
        )
    )

    is DomainNetworkState.Empty -> DomainNetworkState.Empty
    is DomainNetworkState.ErrorNetwork -> mal
}
