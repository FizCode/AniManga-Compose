package dev.fizcode.anime.presentation

import dev.fizcode.anime.MainDispatcherRule
import dev.fizcode.anime.awaitValue
import dev.fizcode.anime.domain.model.Data
import dev.fizcode.anime.domain.model.Node
import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.anime.domain.usecase.AnimeUseCaseGroup
import dev.fizcode.anime.domain.usecase.FetchSeasonalAnimeUseCase
import dev.fizcode.anime.domain.usecase.FetchTopAiringAnimeUseCase
import dev.fizcode.anime.domain.usecase.FetchTopRankingAnimeUseCase
import dev.fizcode.anime.presentation.mapper.SeasonalAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopAiringAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopRankingAnimeUiMapper
import dev.fizcode.anime.presentation.model.AnimeIntent
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.mal.util.Season
import dev.fizcode.mal.util.SeasonHelper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.NsfwFilterProvider
import kotlinx.coroutines.flow.flowOf

class AnimeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeAnimeRepository : AnimeRepository {
        val seasonCalls = AtomicInteger()
        var gate: CompletableDeferred<Unit>? = null
        var seasonResult: () -> DomainNetworkState<SeasonalAnimeDomainModel> = {
            DomainNetworkState.Success(
                SeasonalAnimeDomainModel(listOf(Data(Node(id = 1, title = "Season"))))
            )
        }

        override suspend fun fetchSeasonAnime(
            year: Int,
            season: String,
            sortBy: String,
            limit: Int
        ): DomainNetworkState<SeasonalAnimeDomainModel> {
            seasonCalls.incrementAndGet()
            gate?.await()
            return seasonResult()
        }

        override suspend fun fetchTopAiringAnime(
            rankingType: String,
            limit: Int
        ): DomainNetworkState<TopAiringDomainModel> = DomainNetworkState.Success(
            TopAiringDomainModel(
                listOf(TopAiringDomainModel.Data(TopAiringDomainModel.Node(id = 2, title = "Airing")))
            )
        )

        override suspend fun fetchTopRankingAnime(
            rankingType: String,
            limit: Int
        ): DomainNetworkState<TopRankingDomainModel> = DomainNetworkState.Empty
    }

    private val repository = FakeAnimeRepository()

    private val nsfwFilterProvider = object : NsfwFilterProvider {
        override val nsfwFilter = flowOf(NsfwFilter.SAFE)
    }

    private fun viewModel() = AnimeViewModel(
        animeUseCaseGroup = AnimeUseCaseGroup(
            fetchSeasonAnimeUseCase = FetchSeasonalAnimeUseCase(
                repository,
                object : SeasonHelper {
                    override fun getCurrentSeason() = Season.FALL
                    override fun getCurrentYearForSeason() = 2025
                    override fun getCurrentYear() = 2025
                }
            ),
            fetchTopAiringAnimeUseCase = FetchTopAiringAnimeUseCase(repository),
            fetchTopRankingAnimeUseCase = FetchTopRankingAnimeUseCase(repository)
        ),
        seasonalUiMapper = SeasonalAnimeUiMapper(),
        topAiringUiMapper = TopAiringAnimeUiMapper(),
        topRankingUiMapper = TopRankingAnimeUiMapper(),
        nsfwFilterProvider = nsfwFilterProvider
    )

    @Test
    fun `loads every section when created`() = runBlocking<Unit> {
        val vm = viewModel()

        val state = vm.uiState.awaitValue { !it.isLoading }

        assertEquals("Season", (state.currentSeason as UiState.Success).data.single().title)
        assertEquals("Airing", (state.topAiring as UiState.Success).data.single().title)
        assertEquals(UiState.Empty, state.topRanking)
        assertEquals(1, repository.seasonCalls.get())
    }

    @Test
    fun `a failing section becomes an error without breaking the others`() = runBlocking<Unit> {
        repository.seasonResult = { throw IllegalStateException("boom") }
        val vm = viewModel()

        val state = vm.uiState.awaitValue { !it.isLoading }

        assertEquals("boom", (state.currentSeason as UiState.Error).message)
        assertTrue(state.topAiring is UiState.Success)
    }

    @Test
    fun `refresh reloads every section and stops refreshing when all are done`() = runBlocking<Unit> {
        val vm = viewModel()
        vm.uiState.awaitValue { !it.isLoading }
        val gate = CompletableDeferred<Unit>()
        repository.gate = gate

        vm.onIntent(AnimeIntent.Refresh)

        val refreshing = vm.uiState.value
        assertTrue(refreshing.isRefreshing)
        assertTrue(refreshing.currentSeason is UiState.Loading)
        gate.complete(Unit)
        val settled = vm.uiState.awaitValue { !it.isRefreshing }
        assertFalse(settled.isLoading)
        assertEquals(2, repository.seasonCalls.get())
    }
}
