package dev.fizcode.mediadetailinfo.model

import androidx.compose.runtime.Immutable

@Immutable
data class AnimeStaffUiModel(
    val staffId: Int = 0,
    val name: String = "",
    val role: String = "",
    val image: String = "",
    val url: String = ""
)
