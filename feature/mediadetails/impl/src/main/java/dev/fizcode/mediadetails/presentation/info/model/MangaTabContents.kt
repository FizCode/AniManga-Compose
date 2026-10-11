package dev.fizcode.mediadetails.presentation.info.model

import androidx.compose.runtime.Immutable

@Immutable
internal enum class MangaTabContents(val tabs: String) {
    DETAILS("Details"),
    INFO("Info"),
    CHARACTERS("Characters")
}
