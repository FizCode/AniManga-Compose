package dev.fizcode.mediadetails.domain.repository

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.StaffDomainModel
import kotlinx.coroutines.flow.Flow
import dev.fizcode.mediadetails.domain.model.PagedResult

internal interface MediaDetailsRepository {

    suspend fun fetchAnimeDetails(
        animeId: Int,
        fields: String
    ): DomainNetworkState<AnimeDetailsDomainModel>

    suspend fun fetchAnimeCast(animeId: Int): DomainNetworkState<List<CastDomainModel>>

    suspend fun fetchAnimeStaff(animeId: Int): DomainNetworkState<List<StaffDomainModel>>

    suspend fun fetchAnimeCastPage(
        animeId: Int,
        page: Int,
        perPage: Int
    ): DomainNetworkState<PagedResult<CastDomainModel>>

    suspend fun fetchAnimeStaffPage(
        animeId: Int,
        page: Int,
        perPage: Int
    ): DomainNetworkState<PagedResult<StaffDomainModel>>

    fun isBookmarked(animeId: Int): Flow<Boolean>

    suspend fun bookmarkMedia(bookmarkEntity: BookmarkDomainModel): Long

    suspend fun deleteBookmark(mediaId: Int): Int

}
