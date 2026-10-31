package dev.fizcode.manga.presentation

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.manga.MainDispatcherRule
import dev.fizcode.manga.awaitValue
import dev.fizcode.manga.domain.model.MangaDomainModel
import dev.fizcode.manga.domain.repository.MangaRepository
import dev.fizcode.manga.domain.usecase.FetchMangaRankingUseCase
import dev.fizcode.manga.presentation.mapper.MangaUiMapper
import dev.fizcode.manga.presentation.model.MangaIntent
import dev.fizcode.manga.presentation.model.MangaSort
import dev.fizcode.manga.presentation.model.MangaType
import dev.fizcode.manga.presentation.model.MangaUiModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.NsfwFilterProvider
import kotlinx.coroutines.flow.flowOf

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
            limit: Int
        ): DomainNetworkState<MangaDomainModel> {
            requests += rankingType to limit
            return DomainNetworkState.Success(MangaDomainModel(items))
        }
    }

    private val nsfwFilterProvider = object : NsfwFilterProvider {
        override val nsfwFilter = flowOf(NsfwFilter.SAFE)
    }

    private fun viewModel(repository: MangaRepository) = MangaViewModel(
        fetchMangaRankingUseCase = FetchMangaRankingUseCase(repository),
        mangaUiMapper = MangaUiMapper(),
        nsfwFilterProvider = nsfwFilterProvider
    )

    private suspend fun MangaViewModel.awaitResult(
        predicate: (UiState<List<MangaUiModel>>) -> Boolean
    ) = uiState.awaitValue { predicate(it.result) }.result

    private fun UiState<List<MangaUiModel>>.ids() =
        (this as UiState.Success).data.map { it.id }

    @Test
    fun `starts on All sorted by ranking`() {
        val vm = viewModel(FakeMangaRepository())

        assertEquals(MangaType.ALL, vm.uiState.value.type)
        assertEquals(MangaSort.RANKING, vm.uiState.value.sort)
    }

    @Test
    fun `ranking sort fetches per tab and a popularity sort fetches one list`() = runBlocking<Unit> {
        val repository = FakeMangaRepository()
        val vm = viewModel(repository)

        vm.awaitResult { it is UiState.Success }
        vm.onIntent(MangaIntent.SelectType(MangaType.ONE_SHOT))
        vm.awaitResult { repository.requests.size == 2 && it is UiState.Success }

        vm.onIntent(MangaIntent.SelectSort(MangaSort.POPULARITY))
        vm.awaitResult { repository.requests.size == 3 && it is UiState.Success }

        // Switching the tab while sorted by popularity must not refetch, it only filters.
        vm.onIntent(MangaIntent.SelectType(MangaType.MANGA))
        val filtered = vm.awaitResult { it is UiState.Success && it.data.map { m -> m.id } == listOf(1) }

        assertEquals(listOf(1), filtered.ids())
        assertEquals(
            listOf("all" to 100, "oneshots" to 100, "bypopularity" to 100),
            repository.requests
        )
    }

    @Test
    fun `other tab asks for the maximum and query narrows the titles`() = runBlocking<Unit> {
        val repository = FakeMangaRepository()
        val vm = viewModel(repository)
        vm.awaitResult { it is UiState.Success }

        vm.onIntent(MangaIntent.SelectType(MangaType.OTHER))
        val other = vm.awaitResult { it is UiState.Success && it.data.map { m -> m.id } == listOf(2) }
        assertEquals("all" to 500, repository.requests.last())
        assertEquals(listOf(2), other.ids())

        vm.onIntent(MangaIntent.SelectType(MangaType.ALL))
        vm.awaitResult { it is UiState.Success && it.data.size == 3 }
        vm.onIntent(MangaIntent.QueryChanged("bleach"))
        val result = vm.awaitResult { it is UiState.Success && it.data.size == 2 }

        assertEquals(listOf(2, 3), result.ids())
    }

    @Test
    fun `retry loads the same list again`() = runBlocking<Unit> {
        val repository = FakeMangaRepository()
        val vm = viewModel(repository)
        vm.awaitResult { it is UiState.Success }

        vm.onIntent(MangaIntent.Retry)

        vm.awaitResult { repository.requests.size == 2 && it is UiState.Success }
        assertEquals(repository.requests[0], repository.requests[1])
    }
}
