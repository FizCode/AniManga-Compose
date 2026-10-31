package dev.fizcode.search.presentation

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.search.MainDispatcherRule
import dev.fizcode.search.awaitValue
import dev.fizcode.search.domain.model.SearchAnimeDomainModel
import dev.fizcode.search.domain.repository.SearchRepository
import dev.fizcode.search.domain.usecase.SearchAnimeUseCase
import dev.fizcode.search.presentation.mapper.SearchUiMapper
import dev.fizcode.search.presentation.model.SearchIntent
import dev.fizcode.search.presentation.model.SearchResultUiModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.NsfwFilterProvider
import kotlinx.coroutines.flow.flowOf

class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeSearchRepository(
        var result: () -> DomainNetworkState<SearchAnimeDomainModel>
    ) : SearchRepository {
        val queries = mutableListOf<String>()

        override suspend fun searchAnime(
            query: String,
            limit: Int
        ): DomainNetworkState<SearchAnimeDomainModel> {
            queries += query
            return result()
        }
    }

    private val node = SearchAnimeDomainModel.Node(id = 1, title = "Bleach", mediaType = "tv")

    private val nsfwFilterProvider = object : NsfwFilterProvider {
        override val nsfwFilter = flowOf(NsfwFilter.SAFE)
    }

    private fun viewModel(repository: SearchRepository) = SearchViewModel(
        searchAnimeUseCase = SearchAnimeUseCase(repository),
        searchUiMapper = SearchUiMapper(),
        nsfwFilterProvider = nsfwFilterProvider
    )

    private fun SearchViewModel.type(text: String) {
        onIntent(SearchIntent.QueryChanged(text))
        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
    }

    private suspend fun SearchViewModel.awaitResult(
        predicate: (UiState<List<SearchResultUiModel>>) -> Boolean
    ) = uiState.awaitValue { predicate(it.result) }.result

    @Test
    fun `starts empty and ignores queries shorter than the minimum`() = runBlocking<Unit> {
        val repository = FakeSearchRepository { DomainNetworkState.Empty }
        val vm = viewModel(repository)

        vm.type("bl")

        assertEquals(UiState.Empty, vm.uiState.value.result)
        assertTrue(repository.queries.isEmpty())
    }

    @Test
    fun `long enough query loads then emits mapped results`() = runBlocking<Unit> {
        val repository = FakeSearchRepository {
            DomainNetworkState.Success(SearchAnimeDomainModel(listOf(node)))
        }
        val vm = viewModel(repository)

        vm.type("  bleach ")

        val state = vm.awaitResult { it is UiState.Success }
        assertEquals("Bleach", (state as UiState.Success).data.single().title)
        assertEquals(listOf("bleach"), repository.queries)
    }

    @Test
    fun `repository exception becomes an error state`() = runBlocking<Unit> {
        val repository = FakeSearchRepository { throw IllegalStateException("boom") }
        val vm = viewModel(repository)

        vm.type("bleach")

        val state = vm.awaitResult { it is UiState.Error }
        assertEquals("boom", (state as UiState.Error).message)
    }

    @Test
    fun `empty result maps to empty state`() = runBlocking<Unit> {
        val repository = FakeSearchRepository { DomainNetworkState.Empty }
        val vm = viewModel(repository)

        vm.type("bleach")
        vm.awaitResult { repository.queries.isNotEmpty() }
        vm.awaitResult { it == UiState.Empty }

        assertEquals(UiState.Empty, vm.uiState.value.result)
    }

    @Test
    fun `clearing the query returns to empty`() = runBlocking<Unit> {
        val repository = FakeSearchRepository {
            DomainNetworkState.Success(SearchAnimeDomainModel(listOf(node)))
        }
        val vm = viewModel(repository)

        vm.type("bleach")
        vm.awaitResult { it is UiState.Success }
        vm.type("")

        vm.awaitResult { it == UiState.Empty }
    }

    @Test
    fun `a newer query replaces the pending one before it is searched`() = runBlocking<Unit> {
        val repository = FakeSearchRepository {
            DomainNetworkState.Success(SearchAnimeDomainModel(listOf(node)))
        }
        val vm = viewModel(repository)

        vm.onIntent(SearchIntent.QueryChanged("blea"))
        vm.type("bleach")

        vm.awaitResult { it is UiState.Success }
        assertEquals(listOf("bleach"), repository.queries)
    }
}
