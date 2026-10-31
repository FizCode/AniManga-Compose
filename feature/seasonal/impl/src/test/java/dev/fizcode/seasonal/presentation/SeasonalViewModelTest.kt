package dev.fizcode.seasonal.presentation

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.mal.util.Season
import dev.fizcode.mal.util.SeasonHelper
import dev.fizcode.seasonal.MainDispatcherRule
import dev.fizcode.seasonal.awaitValue
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel
import dev.fizcode.seasonal.domain.repository.SeasonalRepository
import dev.fizcode.seasonal.domain.usecase.FetchSeasonalAnimeUseCase
import dev.fizcode.seasonal.presentation.mapper.SeasonalUiMapper
import dev.fizcode.seasonal.presentation.model.SeasonalIntent
import dev.fizcode.seasonal.presentation.model.SeasonalUiModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.NsfwFilterProvider
import kotlinx.coroutines.flow.flowOf

class SeasonalViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeSeasonalRepository : SeasonalRepository {
        val requests = mutableListOf<Pair<Int, String>>()
        var items = listOf(
            SeasonalDomainModel.Item(id = 1, title = "Bleach"),
            SeasonalDomainModel.Item(id = 2, title = "Naruto")
        )

        override suspend fun fetchSeasonalAnime(
            year: Int,
            season: String,
            sortBy: String,
            limit: Int
        ): DomainNetworkState<SeasonalDomainModel> {
            requests += year to season
            return DomainNetworkState.Success(SeasonalDomainModel(items))
        }
    }

    private class FakeSeasonHelper(private val season: Season) : SeasonHelper {
        override fun getCurrentSeason() = season
        override fun getCurrentYearForSeason() = 2026
        override fun getCurrentYear() = 2026
    }

    private val nsfwFilterProvider = object : NsfwFilterProvider {
        override val nsfwFilter = flowOf(NsfwFilter.SAFE)
    }

    private fun viewModel(
        repository: SeasonalRepository,
        currentSeason: Season = Season.SPRING
    ) = SeasonalViewModel(
        fetchSeasonalAnimeUseCase = FetchSeasonalAnimeUseCase(repository),
        seasonalUiMapper = SeasonalUiMapper(),
        seasonHelper = FakeSeasonHelper(currentSeason),
        nsfwFilterProvider = nsfwFilterProvider
    )

    private suspend fun SeasonalViewModel.awaitResult(
        predicate: (UiState<List<SeasonalUiModel>>) -> Boolean
    ) = uiState.awaitValue { predicate(it.result) }.result

    @Test
    fun `selects the current season and year on start`() {
        val vm = viewModel(FakeSeasonalRepository(), currentSeason = Season.FALL)

        assertEquals(Season.FALL, vm.uiState.value.season)
        assertEquals(2026, vm.uiState.value.year)
        assertEquals(2027, vm.uiState.value.years.first())
    }

    @Test
    fun `loads the current season then reloads when another is selected`() = runBlocking<Unit> {
        val repository = FakeSeasonalRepository()
        val vm = viewModel(repository, currentSeason = Season.SPRING)

        vm.awaitResult { it is UiState.Success }
        vm.onIntent(SeasonalIntent.SelectSeason(Season.WINTER))
        vm.awaitResult { repository.requests.size == 2 && it is UiState.Success }

        assertEquals(listOf(2026 to "spring", 2026 to "winter"), repository.requests)
    }

    @Test
    fun `query filters loaded titles and reports empty when nothing matches`() = runBlocking<Unit> {
        val vm = viewModel(FakeSeasonalRepository())
        vm.awaitResult { it is UiState.Success }

        vm.onIntent(SeasonalIntent.QueryChanged("blea"))
        val filtered = vm.awaitResult { it is UiState.Success && it.data.size == 1 }
        assertEquals("Bleach", (filtered as UiState.Success).data.single().title)

        vm.onIntent(SeasonalIntent.QueryChanged("zzz"))
        assertTrue(vm.awaitResult { it is UiState.Empty } is UiState.Empty)
    }
}
