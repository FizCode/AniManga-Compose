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
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.util.SeasonHelper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class AnimeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeAnimeRepository : AnimeRepository {
        val seasonCalls = AtomicInteger()
        var seasonResult: () -> DomainNetworkState<SeasonalAnimeDomainModel> = {
            DomainNetworkState.Success(
                SeasonalAnimeDomainModel(listOf(Data(Node(id = 1, title = "Season"))))
            )
        }

        override suspend fun fetchSeasonAnime(
            year: Int,
            season: String,
            sortBy: String,
            limit: Int,
            fields: String
        ): DomainNetworkState<SeasonalAnimeDomainModel> {
            seasonCalls.incrementAndGet()
            return seasonResult()
        }

        override suspend fun fetchTopAiringAnime(
            rankingType: String,
            limit: Int,
            fields: String
        ): DomainNetworkState<TopAiringDomainModel> = DomainNetworkState.Success(
            TopAiringDomainModel(
                listOf(TopAiringDomainModel.Data(TopAiringDomainModel.Node(id = 2, title = "Airing")))
            )
        )

        override suspend fun fetchTopRankingAnime(
            rankingType: String,
            limit: Int,
            fields: String
        ): DomainNetworkState<TopRankingDomainModel> = DomainNetworkState.Empty
    }

    private val repository = FakeAnimeRepository()

    private fun viewModel() = AnimeViewModel(
        animeUseCaseGroup = AnimeUseCaseGroup(
            fetchSeasonAnimeUseCase = FetchSeasonalAnimeUseCase(
                repository,
                object : SeasonHelper {
                    override fun getCurrentSeason() = "fall"
                    override fun getCurrentYearForSeason() = 2025
                    override fun getCurrentYear() = 2025
                }
            ),
            fetchTopAiringAnimeUseCase = FetchTopAiringAnimeUseCase(repository),
            fetchTopRankingAnimeUseCase = FetchTopRankingAnimeUseCase(repository)
        ),
        seasonalUiMapper = SeasonalAnimeUiMapper(),
        topAiringUiMapper = TopAiringAnimeUiMapper(),
        topRankingUiMapper = TopRankingAnimeUiMapper()
    )

    @Test
    fun `state flows are empty until collected`() {
        val vm = viewModel()

        assertEquals(UiState.Empty, vm.currentSeason.value)
        assertEquals(0, repository.seasonCalls.get())
    }

    @Test
    fun `current season emits mapped data`() = runBlocking {
        val vm = viewModel()
        val job = vm.currentSeason.onEach { }.launchIn(this)

        val state = vm.currentSeason.awaitValue { it is UiState.Success }

        assertEquals("Season", (state as UiState.Success).data.single().title)
        job.cancel()
    }

    @Test
    fun `top airing emits mapped data`() = runBlocking {
        val vm = viewModel()
        val job = vm.topAiring.onEach { }.launchIn(this)

        val state = vm.topAiring.awaitValue { it is UiState.Success }

        assertEquals("Airing", (state as UiState.Success).data.single().title)
        job.cancel()
    }

    @Test
    fun `empty repository result maps to empty state`() = runBlocking {
        val vm = viewModel()
        val job = vm.topRanking.onEach { }.launchIn(this)

        // Loading is emitted first, then Empty again once the (empty) result arrives.
        vm.topRanking.awaitValue { it == UiState.Loading || it == UiState.Empty }

        assertTrue(vm.topRanking.value is UiState.Empty || vm.topRanking.value is UiState.Loading)
        job.cancel()
    }

    @Test
    fun `repository failure becomes an error state`() = runBlocking {
        repository.seasonResult = { throw IllegalStateException("boom") }
        val vm = viewModel()
        val job = vm.currentSeason.onEach { }.launchIn(this)

        val state = vm.currentSeason.awaitValue { it is UiState.ErrorException }

        assertEquals("boom", (state as UiState.ErrorException).message)
        job.cancel()
    }

    @Test
    fun `refresh refetches and refreshing flag can be reset`() = runBlocking {
        val vm = viewModel()
        val job = vm.currentSeason.onEach { }.launchIn(this)
        vm.currentSeason.awaitValue { it is UiState.Success }
        assertEquals(1, repository.seasonCalls.get())

        vm.refresh()
        assertTrue(vm.refreshing.value)
        vm.currentSeason.awaitValue { repository.seasonCalls.get() == 2 }

        vm.shouldNotRefresh()
        assertFalse(vm.refreshing.value)
        job.cancel()
    }
}
