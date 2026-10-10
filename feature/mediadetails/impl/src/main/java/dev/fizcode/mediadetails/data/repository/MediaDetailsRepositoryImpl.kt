package dev.fizcode.mediadetails.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.processResponse
import dev.fizcode.datasource.local.dao.BookmarkDAO
import dev.fizcode.mediadetails.data.mapper.AniListDomainMapper
import dev.fizcode.mediadetails.data.mapper.AnimeDetailsDomainMapper
import dev.fizcode.mediadetails.data.mapper.BookmarkDomainMapper
import dev.fizcode.mediadetails.data.service.AniListService
import dev.fizcode.mediadetails.data.service.MediaDetailsService
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

internal class MediaDetailsRepositoryImpl(
    private val animeDetailsService: MediaDetailsService,
    private val aniListService: AniListService,
    private val bookmarkDao: BookmarkDAO,
    private val animeDetailsDomainMapper: AnimeDetailsDomainMapper,
    private val aniListDomainMapper: AniListDomainMapper,
    private val bookmarkDomainMapper: BookmarkDomainMapper
) : MediaDetailsRepository {

    override suspend fun fetchAnimeDetails(
        animeId: Int,
        fields: String
    ): DomainNetworkState<AnimeDetailsDomainModel> = coroutineScope {
        val malRequest = async { fetchMalAnimeDetails(animeId = animeId, fields = fields) }
        val jikanRequest = async { fetchJikanAnimeDetails(animeId = animeId) }

        mergeMalWithOptionalJikan(mal = malRequest.await(), jikan = jikanRequest.await())
    }

    private suspend fun fetchMalAnimeDetails(
        animeId: Int,
        fields: String
    ): DomainNetworkState<MalAnimeDetailsDomainModel> = processResponse {
        animeDetailsDomainMapper.mapToMalAnimeDetailsModel(
            animeDetailsService.fetchMalAnimeDetail(animeId = animeId, fields = fields)
        )
    }

    private suspend fun fetchJikanAnimeDetails(
        animeId: Int
    ): DomainNetworkState<JikanAnimeDetailsDomainModel> = processResponse {
        animeDetailsDomainMapper.mapToJikanAnimeDetailsModel(
            detailsResponse = animeDetailsService.fetchJikanAnimeDetail(animeId = animeId)
        )
    }

    override suspend fun fetchAnimeCast(
        animeId: Int
    ): DomainNetworkState<List<JikanCastDomainModel>> =
        fetchJikanCast(animeId).orFallback { fetchAniListCast(animeId) }

    override suspend fun fetchAnimeStaff(
        animeId: Int
    ): DomainNetworkState<List<JikanStaffDomainModel>> =
        fetchJikanStaff(animeId).orFallback { fetchAniListStaff(animeId) }

    private suspend fun fetchJikanCast(
        animeId: Int
    ): DomainNetworkState<List<JikanCastDomainModel>> = processResponse {
        animeDetailsDomainMapper.mapToCastDomainModel(
            cast = animeDetailsService.fetchJikanVoiceActors(animeId = animeId)
        )
    }

    private suspend fun fetchJikanStaff(
        animeId: Int
    ): DomainNetworkState<List<JikanStaffDomainModel>> = processResponse {
        animeDetailsDomainMapper.mapToStaffDomainModel(
            staff = animeDetailsService.fetchJikanStaff(animeId = animeId)
        )
    }

    private suspend fun fetchAniListCast(
        animeId: Int
    ): DomainNetworkState<List<JikanCastDomainModel>> = processResponse {
        aniListDomainMapper.mapToCastDomainModel(aniListService.fetchCast(malId = animeId))
    }

    private suspend fun fetchAniListStaff(
        animeId: Int
    ): DomainNetworkState<List<JikanStaffDomainModel>> = processResponse {
        aniListDomainMapper.mapToStaffDomainModel(aniListService.fetchStaff(malId = animeId))
    }

    override fun isBookmarked(animeId: Int): Flow<Boolean> =
        bookmarkDao.getIsBookmarked(mediaId = animeId)


    override suspend fun bookmarkMedia(bookmarkEntity: BookmarkDomainModel): Long =
        bookmarkDao.setBookmark(
            bookmarkEntity = bookmarkDomainMapper.mapTopBookmarkEntity(data = bookmarkEntity)
        )

    override suspend fun deleteBookmark(mediaId: Int): Int = bookmarkDao.deleteBookmark(mediaId)

}

/**
 * Builds the details model from MAL, treating Jikan as optional.
 *
 * MAL carries everything needed to show the screen, so a Jikan failure only drops the Jikan-only
 * extras. A MAL failure is returned as is, so the caller sees the real error.
 */
internal fun mergeMalWithOptionalJikan(
    mal: DomainNetworkState<MalAnimeDetailsDomainModel>,
    jikan: DomainNetworkState<JikanAnimeDetailsDomainModel>
): DomainNetworkState<AnimeDetailsDomainModel> = when (mal) {
    is DomainNetworkState.Success -> DomainNetworkState.Success(
        AnimeDetailsDomainModel(
            malDomainModel = mal.data,
            jikanDomainModel = (jikan as? DomainNetworkState.Success)?.data
        )
    )

    is DomainNetworkState.Empty -> DomainNetworkState.Empty
    is DomainNetworkState.ErrorNetwork -> mal
}

/**
 * Returns this result when it succeeded, otherwise the [fallback] result if that succeeded.
 *
 * When both fail, this (the primary source's) result is returned, so the caller sees the original
 * error or empty state instead of the fallback's.
 */
internal suspend fun <T> DomainNetworkState<T>.orFallback(
    fallback: suspend () -> DomainNetworkState<T>
): DomainNetworkState<T> {
    if (this is DomainNetworkState.Success) return this
    val fallbackResult = fallback()
    return if (fallbackResult is DomainNetworkState.Success) fallbackResult else this
}
