package dev.fizcode.mediadetailinfo.model

import androidx.compose.runtime.Immutable

@Immutable
data class AnimeCastUiModel(
    val characterId: Int = 0,
    val character: String = "",
    val characterImage: String = "",
    val characterRole: String = "",
    val characterUrl: String = "",
    val voiceActorId: Int = 0,
    val voiceActorName: String = "",
    val voiceActorImage: String = "",
    val voiceActorLang: String = "",
    val voiceActorUrl: String = ""
)
