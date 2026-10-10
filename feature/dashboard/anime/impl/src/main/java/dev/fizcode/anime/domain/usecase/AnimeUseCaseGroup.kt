package dev.fizcode.anime.domain.usecase

internal data class AnimeUseCaseGroup(
    val fetchSeasonAnimeUseCase: FetchSeasonalAnimeUseCase,
    val fetchTopAiringAnimeUseCase: FetchTopAiringAnimeUseCase,
    val fetchTopRankingAnimeUseCase: FetchTopRankingAnimeUseCase
)
