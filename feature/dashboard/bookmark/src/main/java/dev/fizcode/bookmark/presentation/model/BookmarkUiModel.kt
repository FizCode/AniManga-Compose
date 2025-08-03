package dev.fizcode.bookmark.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class BookmarkUiModel(
    val mediaId: Int,
    val mediaType: String,
    val posterPath: String,
    val rating: String,
    val title: String,
    val subTitle: String,
    val studio: String,
    val genre: ImmutableList<String>
)
