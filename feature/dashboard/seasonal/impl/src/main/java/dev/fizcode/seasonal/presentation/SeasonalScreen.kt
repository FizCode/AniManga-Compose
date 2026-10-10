package dev.fizcode.seasonal.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.seasonal.presentation.component.SeasonTabs
import dev.fizcode.seasonal.presentation.component.SeasonalHeader
import dev.fizcode.seasonal.presentation.component.SeasonalListComponent
import dev.fizcode.seasonal.presentation.model.Season
import dev.fizcode.seasonal.presentation.model.SeasonalUiModel
import dev.fizcode.seasonal.presentation.model.dummySeasonalUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SeasonalScreen(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    seasonalViewModel: SeasonalViewModel = koinViewModel()
) {
    val result by seasonalViewModel.seasonalResult.collectAsStateWithLifecycle()
    val season by seasonalViewModel.selectedSeason.collectAsStateWithLifecycle()
    val year by seasonalViewModel.selectedYear.collectAsStateWithLifecycle()

    SeasonalScreenContent(
        queryState = seasonalViewModel.queryState,
        season = season,
        year = year,
        years = seasonalViewModel.availableYears,
        result = result,
        onSeasonSelect = seasonalViewModel::selectSeason,
        onYearSelect = seasonalViewModel::selectYear,
        onCardClick = onCardClick,
        onRetry = seasonalViewModel::retry
    )
}

@Composable
private fun SeasonalScreenContent(
    queryState: TextFieldState,
    season: Season,
    year: Int,
    years: ImmutableList<Int>,
    result: UiState<List<SeasonalUiModel>>,
    onSeasonSelect: (Season) -> Unit,
    onYearSelect: (Int) -> Unit,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SeasonalHeader(
                queryState = queryState,
                year = year,
                years = years,
                onYearSelect = onYearSelect
            )
            Spacer(Modifier.height(8.dp))
            SeasonTabs(selected = season, onSelect = onSeasonSelect)
            Spacer(Modifier.height(8.dp))
            SeasonalListComponent(
                result = result,
                isFiltering = queryState.text.isNotBlank(),
                onCardClick = onCardClick,
                onRetry = onRetry
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SeasonalScreenPreview() {
    SeasonalScreenContent(
        queryState = rememberTextFieldState(),
        season = Season.WINTER,
        year = 2026,
        years = persistentListOf(2027, 2026, 2025),
        result = UiState.Success(listOf(dummySeasonalUiModel, dummySeasonalUiModel.copy(id = 1, rank = 2))),
        onSeasonSelect = {},
        onYearSelect = {},
        onCardClick = { _, _ -> },
        onRetry = {}
    )
}
