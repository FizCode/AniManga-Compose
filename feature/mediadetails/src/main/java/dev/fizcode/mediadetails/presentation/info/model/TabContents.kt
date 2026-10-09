package dev.fizcode.mediadetails.presentation.info.model

import androidx.compose.runtime.Immutable

@Immutable
internal enum class TabContents(val tabs: String) {
    DETAILS("Details"),
    INFO("Info"),
    CAST("Cast"),
    SONGS("Songs")
}
