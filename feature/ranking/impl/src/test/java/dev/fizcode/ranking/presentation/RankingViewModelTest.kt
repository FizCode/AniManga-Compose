package dev.fizcode.ranking.presentation

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.ranking.MainDispatcherRule
import dev.fizcode.ranking.awaitValue
import dev.fizcode.ranking.domain.model.RankingDomainModel
import dev.fizcode.ranking.domain.repository.RankingRepository
import dev.fizcode.ranking.domain.usecase.FetchAnimeRankingUseCase
import dev.fizcode.ranking.presentation.mapper.RankingUiMapper
import dev.fizcode.ranking.presentation.model.RankingIntent
import dev.fizcode.ranking.presentation.model.RankingType
import dev.fizcode.ranking.presentation.model.RankingUiModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.NsfwFilterProvider
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.MutableStateFlow

class RankingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeRankingRepository : RankingRepository {
        val requestedTypes = mutableListOf<String>()
        var result: DomainNetworkState<RankingDomainModel> = DomainNetworkState.Success(
            RankingDomainModel(
                listOf(
                    RankingDomainModel.Item(id = 1, rank = 1, title = "Bleach"),
                    RankingDomainModel.Item(id = 2, rank = 2, title = "Naruto")
                )
            )
        )

        override suspend fun fetchAnimeRanking(
            rankingType: String,
            limit: Int
        ): DomainNetworkState<RankingDomainModel> {
            requestedTypes += rankingType
            return result
        }
    }

    private val repository = FakeRankingRepository()

    private val nsfwFilter = MutableStateFlow(NsfwFilter.SAFE)

    private fun viewModel(type: RankingType = RankingType.TOP_AIRING) = RankingViewModel(
        rankingType = type.apiValue,
        fetchAnimeRankingUseCase = FetchAnimeRankingUseCase(repository),
        rankingUiMapper = RankingUiMapper(),
        nsfwFilterProvider = object : NsfwFilterProvider {
            override val nsfwFilter = this@RankingViewModelTest.nsfwFilter
        }
    )

    private suspend fun RankingViewModel.awaitResult(
        predicate: (UiState<List<RankingUiModel>>) -> Boolean
    ) = uiState.awaitValue { predicate(it.result) }.result

    @Test
    fun `asks for the ranking of its type`() = runBlocking<Unit> {
        val vm = viewModel(RankingType.TOP_RANKING)

        vm.awaitResult { it is UiState.Success }

        assertEquals(listOf("all"), repository.requestedTypes)
    }

    @Test
    fun `typing filters by title and keeps the original ranks`() = runBlocking<Unit> {
        val vm = viewModel()
        vm.awaitResult { it is UiState.Success }

        vm.onIntent(RankingIntent.QueryChanged("nar"))
        val state = vm.awaitResult { it is UiState.Success && it.data.size == 1 }

        val item = (state as UiState.Success).data.single()
        assertEquals("Naruto", item.title)
        assertEquals(2, item.rank)
    }

    @Test
    fun `no match becomes empty`() = runBlocking<Unit> {
        val vm = viewModel()
        vm.awaitResult { it is UiState.Success }

        vm.onIntent(RankingIntent.QueryChanged("zzz"))

        vm.awaitResult { it is UiState.Empty }
    }

    @Test
    fun `retry loads the ranking again`() = runBlocking<Unit> {
        repository.result = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = viewModel()
        vm.awaitResult { it is UiState.Error }
        assertEquals(1, repository.requestedTypes.size)

        vm.onIntent(RankingIntent.Retry)

        vm.awaitResult { repository.requestedTypes.size == 2 }
    }

    @Test
    fun `picking another nsfw level loads the ranking again`() = runBlocking<Unit> {
        val vm = viewModel()
        vm.awaitResult { it is UiState.Success }
        assertEquals(1, repository.requestedTypes.size)

        nsfwFilter.value = NsfwFilter.NOT_SAFE

        vm.awaitResult { repository.requestedTypes.size == 2 }
    }
}
