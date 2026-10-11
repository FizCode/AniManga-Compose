package dev.fizcode.manga.presentation

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshots.Snapshot
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.manga.MainDispatcherRule
import dev.fizcode.manga.awaitValue
import dev.fizcode.manga.domain.model.MangaDomainModel
import dev.fizcode.manga.domain.repository.MangaRepository
import dev.fizcode.manga.domain.usecase.FetchMangaRankingUseCase
import dev.fizcode.manga.presentation.mapper.MangaUiMapper
import dev.fizcode.manga.presentation.model.MangaSort
import dev.fizcode.manga.presentation.model.MangaType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MangaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeMangaRepository : MangaRepository {
        val requests = mutableListOf<Pair<String, Int>>()
        val items = listOf(
            MangaDomainModel.Item(id = 1, title = "Berserk", mediaType = "manga"),
            MangaDomainModel.Item(id = 2, title = "Bleach Novel", mediaType = "light_novel"),
            MangaDomainModel.Item(id = 3, title = "Bleach Short", mediaType = "one_shot")
        )

        override suspend fun fetchMangaRanking(
            rankingType: String,
            limit: Int,
            fields: String
        ): DomainNetworkState<MangaDomainModel> {
            requests += rankingType to limit
            return DomainNetworkState.Success(MangaDomainModel(items))
        }
    }

    private fun viewModel(repository: MangaRepository) = MangaViewModel(
        fetchMangaRankingUseCase = FetchMangaRankingUseCase(repository),
        mangaUiMapper = MangaUiMapper()
    )

    private fun UiState<List<dev.fizcode.manga.presentation.model.MangaUiModel>>.ids() =
        (this as UiState.Success).data.map { it.id }

    @Test
    fun `starts on All sorted by ranking`() {
        val vm = viewModel(FakeMangaRepository())

        assertEquals(MangaType.ALL, vm.selectedType.value)
        assertEquals(MangaSort.RANKING, vm.selectedSort.value)
    }

    @Test
    fun `ranking sort fetches per tab and a popularity sort fetches one list`() = runBlocking {
        val repository = FakeMangaRepository()
        val vm = viewModel(repository)
        val job = vm.mangaResult.onEach { }.launchIn(this)

        vm.mangaResult.awaitValue { it is UiState.Success }
        vm.selectType(MangaType.ONE_SHOT)
        vm.mangaResult.awaitValue { repository.requests.size == 2 && it is UiState.Success }

        vm.selectSort(MangaSort.POPULARITY)
        vm.mangaResult.awaitValue { repository.requests.size == 3 && it is UiState.Success }

        // Switching the tab while sorted by popularity must not refetch, it only filters.
        vm.selectType(MangaType.MANGA)
        val filtered = vm.mangaResult.awaitValue { it is UiState.Success && it.data.map { m -> m.id } == listOf(1) }
        job.cancel()

        assertEquals(listOf(1), filtered.ids())
        assertEquals(
            listOf("all" to 100, "oneshots" to 100, "bypopularity" to 100),
            repository.requests
        )
    }

    @Test
    fun `other tab asks for the maximum and query narrows the titles`() = runBlocking {
        val repository = FakeMangaRepository()
        val vm = viewModel(repository)
        val job = vm.mangaResult.onEach { }.launchIn(this)
        vm.mangaResult.awaitValue { it is UiState.Success }

        vm.selectType(MangaType.OTHER)
        val other = vm.mangaResult.awaitValue { it is UiState.Success && it.data.map { m -> m.id } == listOf(2) }
        assertEquals("all" to 500, repository.requests.last())
        assertEquals(listOf(2), other.ids())

        vm.selectType(MangaType.ALL)
        vm.mangaResult.awaitValue { it is UiState.Success && it.data.size == 3 }
        vm.queryState.setTextAndPlaceCursorAtEnd("bleach")
        Snapshot.sendApplyNotifications()
        val result = vm.mangaResult.awaitValue { it is UiState.Success && it.data.size == 2 }
        job.cancel()

        assertEquals(listOf(2, 3), result.ids())
    }
}
