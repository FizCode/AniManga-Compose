package dev.fizcode.mediadetailinfo.model

import androidx.compose.runtime.Immutable

@Immutable
data class AnimeRelatedUiModel(
    val relatedId: Int = 0,
    val name: String = "",
    val relationType: String = "",
    val image: String = ""
)
