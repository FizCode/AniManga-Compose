package dev.fizcode.mediadetails.presentation.info.model

import androidx.compose.runtime.Immutable

@Immutable
data class MangaCharacterUiModel(
    val characterId: Int = 0,
    val name: String = "",
    val image: String = "",
    val role: String = "",
    val url: String = ""
)
