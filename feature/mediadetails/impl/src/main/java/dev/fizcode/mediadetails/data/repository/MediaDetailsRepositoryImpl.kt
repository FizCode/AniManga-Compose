package dev.fizcode.mediadetails.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.processResponse
import dev.fizcode.database.bookmark.BookmarkLocalDataSource
import dev.fizcode.mediadetails.data.mapper.AniListDomainMapper
import dev.fizcode.mediadetails.data.mapper.AnimeDetailsDomainMapper
import dev.fizcode.mediadetails.data.mapper.BookmarkDomainMapper
import dev.fizcode.mediadetails.data.service.AniListService
import dev.fizcode.mediadetails.data.service.MediaDetailsService
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.domain.model.AniListAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.StaffDomainModel
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

internal class MediaDetailsRepositoryImpl(
    private val animeDetailsService: MediaDetailsService,
    private val aniListService: AniListService,
    private val bookmarkLocalDataSource: BookmarkLocalDataSource,
    private val animeDetailsDomainMapper: AnimeDetailsDomainMapper,
    private val aniListDomainMapper: AniListDomainMapper,
    private val bookmarkDomainMapper: BookmarkDomainMapper
) : MediaDetailsRepository {

    override suspend fun fetchAnimeDetails(
        animeId: Int,
        fields: String
    ): DomainNetworkState<AnimeDetailsDomainModel> = coroutineScope {
        val malRequest = async { fetchMalAnimeDetails(animeId = animeId, fields = fields) }
        val aniListRequest = async { fetchAniListDetails(animeId = animeId) }

        mergeMalWithOptionalAniList(mal = malRequest.await(), aniList = aniListRequest.await())
    }

    private suspend fun fetchMalAnimeDetails(
        animeId: Int,
        fields: String
    ): DomainNetworkState<MalAnimeDetailsDomainModel> = processResponse {
        animeDetailsDomainMapper.mapToMalAnimeDetailsModel(
            animeDetailsService.fetchMalAnimeDetail(animeId = animeId, fields = fields)
        )
    }

    private suspend fun fetchAniListDetails(
        animeId: Int
    ): DomainNetworkState<AniListAnimeDetailsDomainModel> = processResponse {
        aniListDomainMapper.mapToDetailsDomainModel(aniListService.fetchDetails(malId = animeId))
    }

    override suspend fun fetchAnimeCast(
        animeId: Int
    ): DomainNetworkState<List<CastDomainModel>> = processResponse {
        aniListDomainMapper.mapToCastDomainModel(aniListService.fetchCast(malId = animeId))
    }

    override suspend fun fetchAnimeStaff(
        animeId: Int
    ): DomainNetworkState<List<StaffDomainModel>> = processResponse {
        aniListDomainMapper.mapToStaffDomainModel(aniListService.fetchStaff(malId = animeId))
    }

    override fun isBookmarked(animeId: Int): Flow<Boolean> =
        bookmarkLocalDataSource.isBookmarked(mediaId = animeId)


    override suspend fun bookmarkMedia(bookmarkEntity: BookmarkDomainModel): Long =
        bookmarkLocalDataSource.saveBookmark(
            record = bookmarkDomainMapper.mapToBookmarkRecord(data = bookmarkEntity)
        )

    override suspend fun deleteBookmark(mediaId: Int): Int = bookmarkLocalDataSource.deleteBookmark(mediaId)

}

/**
 * Builds the details model from MAL, treating AniList as optional.
 *
 * MAL carries everything needed to show the screen, so an AniList failure only drops the AniList-only
 * extras (producers and themes). A MAL failure is returned as is, so the caller sees the real error.
 */
internal fun mergeMalWithOptionalAniList(
    mal: DomainNetworkState<MalAnimeDetailsDomainModel>,
    aniList: DomainNetworkState<AniListAnimeDetailsDomainModel>
): DomainNetworkState<AnimeDetailsDomainModel> = when (mal) {
    is DomainNetworkState.Success -> DomainNetworkState.Success(
        AnimeDetailsDomainModel(
            malDomainModel = mal.data,
            aniListDomainModel = (aniList as? DomainNetworkState.Success)?.data
        )
    )

    is DomainNetworkState.Empty -> DomainNetworkState.Empty
    is DomainNetworkState.ErrorNetwork -> mal
}
