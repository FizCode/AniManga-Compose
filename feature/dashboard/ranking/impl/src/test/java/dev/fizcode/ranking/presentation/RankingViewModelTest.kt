package dev.fizcode.ranking.presentation

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshots.Snapshot
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.ranking.MainDispatcherRule
import dev.fizcode.ranking.awaitValue
import dev.fizcode.ranking.domain.model.RankingDomainModel
import dev.fizcode.ranking.domain.repository.RankingRepository
import dev.fizcode.ranking.domain.usecase.FetchAnimeRankingUseCase
import dev.fizcode.ranking.presentation.mapper.RankingUiMapper
import dev.fizcode.ranking.presentation.model.RankingType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

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
            limit: Int,
            fields: String
        ): DomainNetworkState<RankingDomainModel> {
            requestedTypes += rankingType
            return result
        }
    }

    private val repository = FakeRankingRepository()

    private fun viewModel(type: RankingType = RankingType.TOP_AIRING) = RankingViewModel(
        rankingType = type.apiValue,
        fetchAnimeRankingUseCase = FetchAnimeRankingUseCase(repository),
        rankingUiMapper = RankingUiMapper()
    )

    @Test
    fun `asks for the ranking of its type`() = runBlocking {
        val vm = viewModel(RankingType.TOP_RANKING)
        val job = vm.rankingResult.onEach { }.launchIn(this)

        vm.rankingResult.awaitValue { it is UiState.Success }

        assertEquals(listOf("all"), repository.requestedTypes)
        job.cancel()
    }

    @Test
    fun `typing filters by title and keeps the original ranks`() = runBlocking {
        val vm = viewModel()
        val job = vm.rankingResult.onEach { }.launchIn(this)
        vm.rankingResult.awaitValue { it is UiState.Success }

        Snapshot.withMutableSnapshot { vm.queryState.setTextAndPlaceCursorAtEnd("nar") }
        val state = vm.rankingResult.awaitValue { it is UiState.Success && it.data.size == 1 }

        val item = (state as UiState.Success).data.single()
        assertEquals("Naruto", item.title)
        assertEquals(2, item.rank)
        job.cancel()
    }

    @Test
    fun `no match becomes empty`() = runBlocking {
        val vm = viewModel()
        val job = vm.rankingResult.onEach { }.launchIn(this)
        vm.rankingResult.awaitValue { it is UiState.Success }

        Snapshot.withMutableSnapshot { vm.queryState.setTextAndPlaceCursorAtEnd("zzz") }

        vm.rankingResult.awaitValue { it is UiState.Empty }
        job.cancel()
    }

    @Test
    fun `retry loads the ranking again`() = runBlocking {
        repository.result = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = viewModel()
        val job = vm.rankingResult.onEach { }.launchIn(this)
        vm.rankingResult.awaitValue { it is UiState.ErrorException }
        assertEquals(1, repository.requestedTypes.size)

        vm.retry()

        vm.rankingResult.awaitValue { repository.requestedTypes.size == 2 }
        job.cancel()
    }
}
