package dev.fizcode.manga.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class MangaUiModel(
    val id: Int,
    val mediaType: String,
    val mediaTypeLabel: String,
    val title: String,
    val posterPath: String,
    val rating: String,
    val subTitle: String,
    val synopsis: String,
    val genre: ImmutableList<String>
)

internal val dummyMangaUiModel = MangaUiModel(
    id = 0,
    mediaType = "light_novel",
    mediaTypeLabel = "Light Novel",
    posterPath = "",
    rating = "5.00",
    title = "BLEACH: Sennen Kessen-hen",
    subTitle = "Vol. 8 Ch. 41 | Finished",
    synopsis = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
    genre = persistentListOf("Action", "Adventure", "Fantasy")
)
