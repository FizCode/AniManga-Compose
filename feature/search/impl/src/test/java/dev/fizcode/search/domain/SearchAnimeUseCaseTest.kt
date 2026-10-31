package dev.fizcode.search.domain

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.search.domain.model.SearchAnimeDomainModel
import dev.fizcode.search.domain.repository.SearchRepository
import dev.fizcode.search.domain.usecase.SearchAnimeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchAnimeUseCaseTest {

    private class FakeSearchRepository(
        private val result: DomainNetworkState<SearchAnimeDomainModel>
    ) : SearchRepository {
        var query: String? = null
        var limit: Int? = null

        override suspend fun searchAnime(
            query: String,
            limit: Int
        ): DomainNetworkState<SearchAnimeDomainModel> {
            this.query = query
            this.limit = limit
            return result
        }
    }

    @Test
    fun `forwards query and limit`() = runTest {
        val repository = FakeSearchRepository(DomainNetworkState.Empty)

        SearchAnimeUseCase(repository)(query = "bleach", limit = 20)

        assertEquals("bleach", repository.query)
        assertEquals(20, repository.limit)
    }

    @Test
    fun `returns the repository result unchanged`() = runTest {
        val success = DomainNetworkState.Success(SearchAnimeDomainModel())
        val error = DomainNetworkState.ErrorNetwork(message = "boom")

        assertEquals(success, SearchAnimeUseCase(FakeSearchRepository(success))("a", 1))
        assertEquals(error, SearchAnimeUseCase(FakeSearchRepository(error))("a", 1))
    }
}
