package dev.fizcode.mediadetails.domain

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.StaffDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import dev.fizcode.mediadetails.domain.model.PagedResult

class FetchAnimeDetailsUseCaseTest {

    private class FakeRepository : MediaDetailsRepository {
        var animeId: Int? = null
        val result = DomainNetworkState.ErrorNetwork(message = "failed")

        override suspend fun fetchAnimeDetails(
            animeId: Int
        ): DomainNetworkState<AnimeDetailsDomainModel> {
            this.animeId = animeId
            return result
        }

        override suspend fun fetchAnimeCast(animeId: Int) =
            DomainNetworkState.Empty as DomainNetworkState<List<CastDomainModel>>

        override suspend fun fetchAnimeStaff(animeId: Int) =
            DomainNetworkState.Empty as DomainNetworkState<List<StaffDomainModel>>

        override suspend fun fetchAnimeCastPage(animeId: Int, page: Int, perPage: Int) =
            DomainNetworkState.Empty as DomainNetworkState<PagedResult<CastDomainModel>>

        override suspend fun fetchAnimeStaffPage(animeId: Int, page: Int, perPage: Int) =
            DomainNetworkState.Empty as DomainNetworkState<PagedResult<StaffDomainModel>>

        override fun isBookmarked(animeId: Int): Flow<Boolean> = flowOf(false)
        override suspend fun bookmarkMedia(bookmarkEntity: BookmarkDomainModel): Long = 0L
        override suspend fun deleteBookmark(mediaId: Int): Int = 0
    }

    @Test
    fun `forwards the anime id and returns the result`() = runTest {
        val repository = FakeRepository()

        val result = FetchAnimeDetailsUseCase(repository)(animeId = 42)

        assertEquals(42, repository.animeId)
        assertEquals(repository.result, result)
    }
}
