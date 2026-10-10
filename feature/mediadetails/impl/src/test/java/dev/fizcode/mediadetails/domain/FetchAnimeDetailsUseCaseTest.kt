package dev.fizcode.mediadetails.domain

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FetchAnimeDetailsUseCaseTest {

    private class FakeRepository : MediaDetailsRepository {
        var animeId: Int? = null
        var fields: String? = null
        val result = DomainNetworkState.ErrorNetwork(message = "failed")

        override suspend fun fetchAnimeDetails(
            animeId: Int,
            fields: String
        ): DomainNetworkState<AnimeDetailsDomainModel> {
            this.animeId = animeId
            this.fields = fields
            return result
        }

        override suspend fun fetchAnimeCast(animeId: Int) =
            DomainNetworkState.Empty as DomainNetworkState<List<JikanCastDomainModel>>

        override suspend fun fetchAnimeStaff(animeId: Int) =
            DomainNetworkState.Empty as DomainNetworkState<List<JikanStaffDomainModel>>

        override fun isBookmarked(animeId: Int): Flow<Boolean> = flowOf(false)
        override suspend fun bookmarkMedia(bookmarkEntity: BookmarkDomainModel): Long = 0L
        override suspend fun deleteBookmark(mediaId: Int): Int = 0
    }

    @Test
    fun `forwards the anime id with a field list and returns the result`() = runTest {
        val repository = FakeRepository()

        val result = FetchAnimeDetailsUseCase(repository)(animeId = 42)

        assertEquals(42, repository.animeId)
        assertTrue(repository.fields.orEmpty().split(",").size > 1)
        assertEquals(repository.result, result)
    }
}
