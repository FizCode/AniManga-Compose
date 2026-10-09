package dev.fizcode.mediadetails.presentation.info.model

import androidx.compose.runtime.Immutable

@Immutable
data class AnimeRelatedUiModel(
    val relatedId: Int = 0,
    val name: String = "",
    val relationType: String = "",
    val url: String = ""
)
