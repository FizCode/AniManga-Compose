package dev.fizcode.anime.presentation.model

import kotlinx.collections.immutable.persistentListOf

internal val dummySeasonalUiModel = SeasonalUiModel(
    id= 0,
    mediaType = "tv",
    title = "BLEACH: Sennen Kessen-hen",
    posterPath = "",
    releaseInfo = "Eps. 10 of 12 in 6d 2h",
    rating = "5.00",
    studio = "Toei Animation",
    synopsis = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam varius mi at neque consequat placerat. Nunc nisl nisi, tempus at consequat sodales, imperdiet sit amet dui.",
    genre = persistentListOf("Action", "Adventure", "Comedy")
)

internal val dummyTopAiringUiModel = TopAiringUiModel(
    id= 0,
    posterPath = "",
    rating = "5.00",
    title = "BLEACH: Sennen Kessen-hen"
)

internal val dummyTopRankingUiModel = TopRankingUiModel(
    id= 0,
    posterPath = "",
    rating = "5.00",
    title = "BLEACH: Sennen Kessen-hen",
    subTitle = "TV | Eps. 10 of 12 in 6d 2h",
    studio = "Toei Animation",
    genre = persistentListOf("Action", "Adventure", "Comedy")
)
