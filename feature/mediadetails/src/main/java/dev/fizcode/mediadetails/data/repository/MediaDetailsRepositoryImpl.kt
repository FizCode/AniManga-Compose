package dev.fizcode.mediadetails.data.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.processResponse
import dev.fizcode.common.base.domainhandler.combineMalAndJikan
import dev.fizcode.datasource.local.dao.BookmarkDAO
import dev.fizcode.mediadetails.data.mapper.AnimeDetailsDomainMapper
import dev.fizcode.mediadetails.data.mapper.BookmarkDomainMapper
import dev.fizcode.mediadetails.data.service.MediaDetailsService
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import kotlinx.coroutines.flow.Flow

internal class MediaDetailsRepositoryImpl(
    private val animeDetailsService: MediaDetailsService,
    private val bookmarkDao: BookmarkDAO,
    private val animeDetailsDomainMapper: AnimeDetailsDomainMapper,
    private val bookmarkDomainMapper: BookmarkDomainMapper
) : MediaDetailsRepository {

    override suspend fun fetchAnimeDetails(
        animeId: Int,
        fields: String
    ): DomainNetworkState<AnimeDetailsDomainModel> {
        val malRequest = fetchMalAnimeDetails(animeId = animeId, fields = fields)
        val jikanRequest = fetchJikanAnimeDetails(animeId = animeId)

        return combineMalAndJikan(
            domain1 = { malRequest },
            domain2 = { jikanRequest },
            returnModel = { mal, jikan ->
                AnimeDetailsDomainModel(
                    malDomainModel = mal,
                    jikanDomainModel = jikan
                )
            }
        )
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
    ): DomainNetworkState<List<JikanCastDomainModel>> = processResponse {
        animeDetailsDomainMapper.mapToCastDomainModel(
            cast = animeDetailsService.fetchJikanVoiceActors(animeId = animeId)
        )
    }

    override suspend fun fetchAnimeStaff(
        animeId: Int
    ): DomainNetworkState<List<JikanStaffDomainModel>> = processResponse {
        animeDetailsDomainMapper.mapToStaffDomainModel(
            staff = animeDetailsService.fetchJikanStaff(animeId = animeId)
        )
    }

    override fun isBookmarked(animeId: Int): Flow<Boolean> =
        bookmarkDao.getIsBookmarked(mediaId = animeId)


    override suspend fun bookmarkMedia(bookmarkEntity: BookmarkDomainModel): Long =
        bookmarkDao.setBookmark(
            bookmarkEntity = bookmarkDomainMapper.mapTopBookmarkEntity(data = bookmarkEntity)
        )

    override suspend fun deleteBookmark(mediaId: Int): Int = bookmarkDao.deleteBookmark(mediaId)

}
