package dev.fizcode.bookmark.presentation

import dev.fizcode.bookmark.MainDispatcherRule
import dev.fizcode.bookmark.awaitValue
import dev.fizcode.bookmark.data.model.BookmarkDomainModel
import dev.fizcode.bookmark.domain.repository.BookmarkRepository
import dev.fizcode.bookmark.presentation.mapper.BookmarkUiMapper
import dev.fizcode.common.base.callhandler.DomainLocalState
import dev.fizcode.common.base.callhandler.UiState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookmarkViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeBookmarkRepository(
        private val states: Flow<DomainLocalState<List<BookmarkDomainModel>>>
    ) : BookmarkRepository {
        override fun getAllBookmarks() = states
    }

    private val bookmark = BookmarkDomainModel(
        mediaId = 1,
        mediaType = "TV",
        posterPath = "poster",
        rating = "8.0",
        title = "Bleach",
        episodes = "12 Episodes",
        status = "Finished",
        studio = "Pierrot",
        genres = "Action, Comedy"
    )

    private fun viewModel(states: Flow<DomainLocalState<List<BookmarkDomainModel>>>) =
        BookmarkViewModel(FakeBookmarkRepository(states), BookmarkUiMapper())

    @Test
    fun `success state is mapped to ui models`() = runBlocking {
        val vm = viewModel(flowOf(DomainLocalState.Success(listOf(bookmark))))
        val job = vm.bookmarks.onEach { }.launchIn(this)

        val state = vm.bookmarks.awaitValue { it is UiState.Success }

        val ui = (state as UiState.Success).data.single()
        assertEquals("Bleach", ui.title)
        assertEquals("TV | 12 Episodes | Finished", ui.subTitle)
        assertEquals(listOf("Action", "Comedy"), ui.genre.toList())
        job.cancel()
    }

    @Test
    fun `empty database emits loading then empty`() = runBlocking {
        val gate = CompletableDeferred<Unit>()
        val vm = viewModel(flow {
            gate.await()
            emit(DomainLocalState.Empty)
        })
        val job = vm.bookmarks.onEach { }.launchIn(this)

        vm.bookmarks.awaitValue { it == UiState.Loading }
        gate.complete(Unit)
        vm.bookmarks.awaitValue { it == UiState.Empty }

        assertEquals(UiState.Empty, vm.bookmarks.value)
        job.cancel()
    }

    @Test
    fun `database error becomes an error state`() = runBlocking {
        val failure = IllegalStateException("db down")
        val vm = viewModel(flowOf(DomainLocalState.Error(failure)))
        val job = vm.bookmarks.onEach { }.launchIn(this)

        val state = vm.bookmarks.awaitValue { it is UiState.ErrorException }

        assertEquals(failure, (state as UiState.ErrorException).error)
        assertEquals("db down", state.message)
        job.cancel()
    }

    @Test
    fun `later database updates replace the list`() = runBlocking {
        val updated = bookmark.copy(mediaId = 2, title = "Naruto")
        val vm = viewModel(
            flowOf(
                DomainLocalState.Success(listOf(bookmark)),
                DomainLocalState.Success(listOf(bookmark, updated))
            )
        )
        val job = vm.bookmarks.onEach { }.launchIn(this)

        val state = vm.bookmarks.awaitValue { it is UiState.Success && it.data.size == 2 }

        assertEquals(listOf("Bleach", "Naruto"), (state as UiState.Success).data.map { it.title })
        job.cancel()
    }
}
