package dev.fizcode.mediadetails.presentation

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.mediadetails.MainDispatcherRule
import dev.fizcode.mediadetails.awaitValue
import dev.fizcode.mediadetails.domain.model.PagedResult
import dev.fizcode.mediadetails.presentation.people.PagedItems
import dev.fizcode.mediadetails.presentation.people.PagedListViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class PagedListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    /** Pages of numbers, the answer of each page can be changed per test. */
    private class FakePages {
        val requestedPages = mutableListOf<Int>()
        val results = mutableMapOf<Int, DomainNetworkState<PagedResult<Int>>>()

        suspend fun fetch(page: Int): DomainNetworkState<PagedResult<Int>> {
            requestedPages += page
            return results[page] ?: DomainNetworkState.Empty
        }
    }

    private class NumbersViewModel(pages: FakePages) : PagedListViewModel<Int, String>(
        fetchPage = pages::fetch,
        mapPage = { numbers -> numbers.map { "item $it" } }
    )

    private val pages = FakePages()

    private fun page(vararg numbers: Int, hasNext: Boolean) =
        DomainNetworkState.Success(PagedResult(numbers.toList(), hasNext))

    private fun UiState<PagedItems<String>>.items() = (this as UiState.Success).data.items.toList()

    @Test
    fun `the first page is loaded on start`() = runBlocking {
        pages.results[1] = page(1, 2, hasNext = true)

        val vm = NumbersViewModel(pages)
        val state = vm.state.awaitValue { it is UiState.Success }

        assertEquals(listOf("item 1", "item 2"), state.items())
        assertTrue((state as UiState.Success).data.hasNextPage)
        assertEquals(listOf(1), pages.requestedPages)
    }

    @Test
    fun `loading more adds the next page after the loaded ones`() = runBlocking {
        pages.results[1] = page(1, 2, hasNext = true)
        pages.results[2] = page(3, hasNext = false)
        val vm = NumbersViewModel(pages)
        vm.state.awaitValue { it is UiState.Success }

        vm.loadMore()
        val state = vm.state.awaitValue { it is UiState.Success && it.data.items.size == 3 }

        assertEquals(listOf("item 1", "item 2", "item 3"), state.items())
        assertFalse((state as UiState.Success).data.hasNextPage)
        assertEquals(listOf(1, 2), pages.requestedPages)
    }

    @Test
    fun `nothing more is loaded when there is no next page`() = runBlocking {
        pages.results[1] = page(1, hasNext = false)
        val vm = NumbersViewModel(pages)
        vm.state.awaitValue { it is UiState.Success }

        vm.loadMore()
        vm.loadMore()

        assertEquals(listOf(1), pages.requestedPages)
    }

    @Test
    fun `an empty first page is an empty list`() = runBlocking {
        pages.results[1] = page(hasNext = false)

        val vm = NumbersViewModel(pages)

        assertEquals(UiState.Empty, vm.state.awaitValue { it is UiState.Empty })
    }

    @Test
    fun `a failing first page is an error and retry loads it again`() = runBlocking {
        pages.results[1] = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = NumbersViewModel(pages)
        vm.state.awaitValue { it is UiState.Error }

        pages.results[1] = page(1, hasNext = false)
        vm.retry()
        val state = vm.state.awaitValue { it is UiState.Success }

        assertEquals(listOf("item 1"), state.items())
        assertEquals(listOf(1, 1), pages.requestedPages)
    }

    @Test
    fun `a failing next page keeps the loaded items and retry loads that page again`() = runBlocking {
        pages.results[1] = page(1, hasNext = true)
        pages.results[2] = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = NumbersViewModel(pages)
        vm.state.awaitValue { it is UiState.Success }

        vm.loadMore()
        val failed = vm.state.awaitValue { it is UiState.Success && it.data.loadMoreFailed }

        assertEquals(listOf("item 1"), failed.items())
        assertFalse((failed as UiState.Success).data.isLoadingMore)

        pages.results[2] = page(2, hasNext = false)
        vm.retry()
        val loaded = vm.state.awaitValue { it is UiState.Success && it.data.items.size == 2 }

        assertEquals(listOf("item 1", "item 2"), loaded.items())
        assertEquals(listOf(1, 2, 2), pages.requestedPages)
    }
}
