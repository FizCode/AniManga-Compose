package dev.fizcode.ranking.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import dev.fizcode.common.settings.MediaTitles

@Immutable
internal data class RankingUiModel(
    val id: Int,
    val rank: Int,
    val mediaType: String,
    val title: String,
    val posterPath: String,
    val rating: String,
    val releaseInfo: String,
    val studio: String,
    val synopsis: String,
    val genre: ImmutableList<String>,
    val titles: MediaTitles = MediaTitles(title)
)

internal val dummyRankingUiModel = RankingUiModel(
    id = 0,
    rank = 1,
    mediaType = "tv",
    posterPath = "",
    rating = "5.00",
    title = "BLEACH: Sennen Kessen-hen",
    releaseInfo = "Wednesday, 23:00 (JST)",
    studio = "Pierrot",
    synopsis = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam varius mi at neque consequat placerat.",
    genre = persistentListOf("Action", "Adventure", "Fantasy", "Comedy")
)
