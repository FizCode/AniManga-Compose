package dev.fizcode.anime.domain.model

internal data class TopAiringDomainModel(
    val data: List<Data> = emptyList(),
    val paging: Paging = Paging(),
    val season: Season = Season()
) {
    data class Data(
        val node: Node = Node()
    )

    data class Node(
        val id: Int = 0,
        val mediaType: String = "",
        val title: String = "",
        val mainPicture: MainPicture = MainPicture(),
        val mean: Double = 0.0
    )

    data class MainPicture(
        val medium: String = "",
        val large: String = ""
    )

    data class Paging(
        val next: String = ""
    )

    data class Season(
        val year: Int = 0,
        val season: String = ""
    )
}
