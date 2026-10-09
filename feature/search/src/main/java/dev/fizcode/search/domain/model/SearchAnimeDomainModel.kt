package dev.fizcode.search.domain.model

internal data class SearchAnimeDomainModel(
    val data: List<Node> = emptyList()
) {
    data class Node(
        val id: Int = 0,
        val mediaType: String = "",
        val title: String = "",
        val posterPath: String = "",
        val mean: Double = 0.0,
        val status: String = "",
        val numEpisodes: Int = 0,
        val genres: List<String> = emptyList()
    )
}
