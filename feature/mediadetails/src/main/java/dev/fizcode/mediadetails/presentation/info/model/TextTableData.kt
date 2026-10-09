package dev.fizcode.mediadetails.presentation.info.model

internal data class TextTableData(
    val title: String,
    val desc: String = "",
    val listDesc: List<String> = emptyList(),
    val link: List<String> = emptyList()
)
