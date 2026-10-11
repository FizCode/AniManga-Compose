package dev.fizcode.manga.domain.model

internal data class MangaDomainModel(
    val items: List<Item> = emptyList()
) {
    data class Item(
        val id: Int = 0,
        val title: String = "",
        val posterPath: String = "",
        val mean: Double = 0.0,
        val mediaType: String = "",
        val status: String = "",
        val numVolumes: Int = 0,
        val numChapters: Int = 0,
        val synopsis: String = "",
        val genres: List<String> = emptyList()
    )
}
