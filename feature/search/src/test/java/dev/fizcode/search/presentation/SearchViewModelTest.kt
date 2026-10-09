package dev.fizcode.search.presentation

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshots.Snapshot
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.search.MainDispatcherRule
import dev.fizcode.search.awaitValue
import dev.fizcode.search.domain.model.SearchAnimeDomainModel
import dev.fizcode.search.domain.repository.SearchRepository
import dev.fizcode.search.domain.usecase.SearchAnimeUseCase
import dev.fizcode.search.presentation.mapper.SearchUiMapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeSearchRepository(
        var result: () -> DomainNetworkState<SearchAnimeDomainModel>
    ) : SearchRepository {
        val queries = mutableListOf<String>()

        override suspend fun searchAnime(
            query: String,
            limit: Int,
            fields: String
        ): DomainNetworkState<SearchAnimeDomainModel> {
            queries += query
            return result()
        }
    }

    private val node = SearchAnimeDomainModel.Node(id = 1, title = "Bleach", mediaType = "tv")

    private fun viewModel(repository: SearchRepository) = SearchViewModel(
        searchAnimeUseCase = SearchAnimeUseCase(repository),
        searchUiMapper = SearchUiMapper()
    )

    private fun SearchViewModel.type(text: String) {
        queryState.setTextAndPlaceCursorAtEnd(text)
        // snapshotFlow only sees the write once the apply notification is delivered.
        Snapshot.sendApplyNotifications()
    }

    @Test
    fun `starts empty and ignores queries shorter than the minimum`() = runBlocking {
        val repository = FakeSearchRepository { DomainNetworkState.Empty }
        val vm = viewModel(repository)
        val job = vm.searchResult.onEach { }.launchIn(this)

        vm.type("bl")
        Snapshot.sendApplyNotifications()

        assertEquals(UiState.Empty, vm.searchResult.value)
        assertTrue(repository.queries.isEmpty())
        job.cancel()
    }

    @Test
    fun `long enough query loads then emits mapped results`() = runBlocking {
        val repository = FakeSearchRepository {
            DomainNetworkState.Success(SearchAnimeDomainModel(listOf(node)))
        }
        val vm = viewModel(repository)
        val job = vm.searchResult.onEach { }.launchIn(this)

        vm.type("  bleach ")

        val state = vm.searchResult.awaitValue { it is UiState.Success }
        assertEquals("Bleach", (state as UiState.Success).data.single().title)
        assertEquals(listOf("bleach"), repository.queries)
        job.cancel()
    }

    @Test
    fun `repository exception becomes an error state`() = runBlocking {
        val repository = FakeSearchRepository { throw IllegalStateException("boom") }
        val vm = viewModel(repository)
        val job = vm.searchResult.onEach { }.launchIn(this)

        vm.type("bleach")

        val state = vm.searchResult.awaitValue { it is UiState.ErrorException }
        assertEquals("boom", (state as UiState.ErrorException).message)
        job.cancel()
    }

    @Test
    fun `empty result maps to empty state`() = runBlocking {
        val repository = FakeSearchRepository { DomainNetworkState.Empty }
        val vm = viewModel(repository)
        val job = vm.searchResult.onEach { }.launchIn(this)

        vm.type("bleach")
        vm.searchResult.awaitValue { repository.queries.isNotEmpty() }
        vm.searchResult.awaitValue { it == UiState.Empty }

        assertEquals(UiState.Empty, vm.searchResult.value)
        job.cancel()
    }

    @Test
    fun `clearing the query returns to empty`() = runBlocking {
        val repository = FakeSearchRepository {
            DomainNetworkState.Success(SearchAnimeDomainModel(listOf(node)))
        }
        val vm = viewModel(repository)
        val job = vm.searchResult.onEach { }.launchIn(this)

        vm.type("bleach")
        vm.searchResult.awaitValue { it is UiState.Success }
        vm.type("")

        vm.searchResult.awaitValue { it == UiState.Empty }
        job.cancel()
    }
}
