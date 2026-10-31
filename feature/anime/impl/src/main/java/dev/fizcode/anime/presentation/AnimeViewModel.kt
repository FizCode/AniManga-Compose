package dev.fizcode.anime.presentation

import dev.fizcode.anime.domain.usecase.AnimeUseCaseGroup
import dev.fizcode.anime.presentation.mapper.SeasonalAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopAiringAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopRankingAnimeUiMapper
import dev.fizcode.anime.presentation.model.AnimeIntent
import dev.fizcode.anime.presentation.model.AnimeUiState
import dev.fizcode.anime.util.Constant
import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.changes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.merge

internal class AnimeViewModel(
    private val animeUseCaseGroup: AnimeUseCaseGroup,
    private val seasonalUiMapper: SeasonalAnimeUiMapper,
    private val topAiringUiMapper: TopAiringAnimeUiMapper,
    private val topRankingUiMapper: TopRankingAnimeUiMapper,
    nsfwFilterProvider: NsfwFilterProvider
) : MviViewModel<AnimeUiState, AnimeIntent, Nothing>(AnimeUiState()) {

    private val refreshes = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        val reloads = merge(refreshes, nsfwFilterProvider.changes())

        loadSection(
            reloads = reloads,
            mapper = seasonalUiMapper::mapToSeasonalAnimeUiModel,
            call = { animeUseCaseGroup.fetchSeasonAnimeUseCase(Constant.LIMIT_5) },
            reduce = { copy(currentSeason = it).settled() }
        )
        loadSection(
            reloads = reloads,
            mapper = topAiringUiMapper::mapToAiringAnimeUiModel,
            call = { animeUseCaseGroup.fetchTopAiringAnimeUseCase(Constant.LIMIT_5) },
            reduce = { copy(topAiring = it).settled() }
        )
        loadSection(
            reloads = reloads,
            mapper = topRankingUiMapper::mapToTopRankingAnimeUiModel,
            call = { animeUseCaseGroup.fetchTopRankingAnimeUseCase(Constant.LIMIT_10) },
            reduce = { copy(topRanking = it).settled() }
        )
    }

    override fun onIntent(intent: AnimeIntent) {
        when (intent) {
            AnimeIntent.Refresh -> {
                setState { refreshing() }
                refreshes.tryEmit(Unit)
            }
        }
    }
}
