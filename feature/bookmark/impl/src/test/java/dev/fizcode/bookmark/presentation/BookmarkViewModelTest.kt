package dev.fizcode.bookmark.presentation

import dev.fizcode.bookmark.MainDispatcherRule
import dev.fizcode.bookmark.awaitValue
import dev.fizcode.bookmark.data.model.BookmarkDomainModel
import dev.fizcode.bookmark.domain.repository.BookmarkRepository
import dev.fizcode.bookmark.presentation.mapper.BookmarkUiMapper
import dev.fizcode.bookmark.presentation.model.BookmarkIntent
import dev.fizcode.bookmark.presentation.model.BookmarkTab
import dev.fizcode.bookmark.presentation.model.BookmarkUiModel
import dev.fizcode.common.base.callhandler.DomainLocalState
import dev.fizcode.common.base.callhandler.UiState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
        genres = listOf("Action", "Comedy")
    )

    private fun viewModel(states: Flow<DomainLocalState<List<BookmarkDomainModel>>>) =
        BookmarkViewModel(FakeBookmarkRepository(states), BookmarkUiMapper())

    private suspend fun BookmarkViewModel.awaitBookmarks(
        predicate: (UiState<List<BookmarkUiModel>>) -> Boolean
    ) = uiState.awaitValue { predicate(it.allBookmarks) }.allBookmarks

    private suspend fun BookmarkViewModel.awaitVisible(
        predicate: (UiState<List<BookmarkUiModel>>) -> Boolean
    ) = uiState.awaitValue { predicate(it.bookmarks) }.bookmarks

    @Test
    fun `success state is mapped to ui models`() = runBlocking {
        val vm = viewModel(flowOf(DomainLocalState.Success(listOf(bookmark))))
        val state = vm.awaitBookmarks { it is UiState.Success }

        val ui = (state as UiState.Success).data.single()
        assertEquals("Bleach", ui.title)
        assertEquals("TV | 12 Episodes | Finished", ui.subTitle)
        assertEquals(listOf("Action", "Comedy"), ui.genre.toList())
    }

    @Test
    fun `empty database emits loading then empty`() = runBlocking {
        val gate = CompletableDeferred<Unit>()
        val vm = viewModel(flow {
            gate.await()
            emit(DomainLocalState.Empty)
        })
        vm.awaitBookmarks { it == UiState.Loading }
        gate.complete(Unit)
        vm.awaitBookmarks { it == UiState.Empty }

        assertEquals(UiState.Empty, vm.uiState.value.allBookmarks)
    }

    @Test
    fun `database error becomes an error state`() = runBlocking {
        val failure = IllegalStateException("db down")
        val vm = viewModel(flowOf(DomainLocalState.Error(failure)))
        val state = vm.awaitBookmarks { it is UiState.Error }

        assertEquals(failure, (state as UiState.Error).cause)
        assertEquals("db down", state.message)
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
        val state = vm.awaitBookmarks { it is UiState.Success && it.data.size == 2 }

        assertEquals(listOf("Bleach", "Naruto"), (state as UiState.Success).data.map { it.title })
    }

    @Test
    fun `tab and query narrow the visible bookmarks`() = runBlocking<Unit> {
        val manga = bookmark.copy(mediaId = 2, mediaType = "Manga", title = "Berserk")
        val vm = viewModel(flowOf(DomainLocalState.Success(listOf(bookmark, manga))))

        val anime = vm.awaitVisible { it is UiState.Success }
        assertEquals(listOf("Bleach"), (anime as UiState.Success).data.map { it.title })

        vm.onIntent(BookmarkIntent.SelectTab(BookmarkTab.MANGA))
        val mangaTab = vm.awaitVisible { it is UiState.Success && it.data.single().title == "Berserk" }
        assertEquals(1, (mangaTab as UiState.Success).data.size)

        vm.onIntent(BookmarkIntent.QueryChanged("zzz"))
        vm.awaitVisible { it == UiState.Empty }

        vm.onIntent(BookmarkIntent.QueryChanged("bers"))
        vm.awaitVisible { it is UiState.Success }
    }
}
