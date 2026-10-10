package dev.fizcode.seasonal.presentation

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshots.Snapshot
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.util.SeasonHelper
import dev.fizcode.seasonal.MainDispatcherRule
import dev.fizcode.seasonal.awaitValue
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel
import dev.fizcode.seasonal.domain.repository.SeasonalRepository
import dev.fizcode.seasonal.domain.usecase.FetchSeasonalAnimeUseCase
import dev.fizcode.seasonal.presentation.mapper.SeasonalUiMapper
import dev.fizcode.seasonal.presentation.model.Season
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

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
            limit: Int,
            fields: String
        ): DomainNetworkState<SeasonalDomainModel> {
            requests += year to season
            return DomainNetworkState.Success(SeasonalDomainModel(items))
        }
    }

    private class FakeSeasonHelper(private val season: String) : SeasonHelper {
        override fun getCurrentSeason() = season
        override fun getCurrentYearForSeason() = 2026
        override fun getCurrentYear() = 2026
    }

    private fun viewModel(
        repository: SeasonalRepository,
        currentSeason: String = "spring"
    ) = SeasonalViewModel(
        fetchSeasonalAnimeUseCase = FetchSeasonalAnimeUseCase(repository),
        seasonalUiMapper = SeasonalUiMapper(),
        seasonHelper = FakeSeasonHelper(currentSeason)
    )

    @Test
    fun `selects the current season and year on start`() {
        val vm = viewModel(FakeSeasonalRepository(), currentSeason = "fall")

        assertEquals(Season.FALL, vm.selectedSeason.value)
        assertEquals(2026, vm.selectedYear.value)
        assertEquals(2027, vm.availableYears.first())
    }

    @Test
    fun `loads the current season then reloads when another is selected`() = runBlocking {
        val repository = FakeSeasonalRepository()
        val vm = viewModel(repository, currentSeason = "spring")
        val job = vm.seasonalResult.onEach { }.launchIn(this)

        vm.seasonalResult.awaitValue { it is UiState.Success }
        vm.selectSeason(Season.WINTER)
        vm.seasonalResult.awaitValue { repository.requests.size == 2 && it is UiState.Success }
        job.cancel()

        assertEquals(listOf(2026 to "spring", 2026 to "winter"), repository.requests)
    }

    @Test
    fun `query filters loaded titles and reports empty when nothing matches`() = runBlocking {
        val vm = viewModel(FakeSeasonalRepository())
        val job = vm.seasonalResult.onEach { }.launchIn(this)
        vm.seasonalResult.awaitValue { it is UiState.Success }

        vm.queryState.setTextAndPlaceCursorAtEnd("blea")
        Snapshot.sendApplyNotifications()
        val filtered = vm.seasonalResult.awaitValue { it is UiState.Success && it.data.size == 1 }
        assertEquals("Bleach", (filtered as UiState.Success).data.single().title)

        vm.queryState.setTextAndPlaceCursorAtEnd("zzz")
        Snapshot.sendApplyNotifications()
        assertTrue(vm.seasonalResult.awaitValue { it is UiState.Empty } is UiState.Empty)
        job.cancel()
    }
}
