package dev.fizcode.mediadetails.domain.model

internal data class MalMangaDetailsDomainModel(
    val alternativeTitles: AlternativeTitles = AlternativeTitles(),
    val authors: List<Author> = emptyList(),
    val background: String = "",
    val endDate: String = "",
    val genres: List<Genre> = emptyList(),
    val id: Int = 0,
    val mainPicture: MainPicture = MainPicture(),
    val mean: Double = 0.0,
    val mediaType: String = "",
    val numChapters: Int = 0,
    val numListUsers: Int = 0,
    val numScoringUsers: Int = 0,
    val numVolumes: Int = 0,
    val pictures: List<Picture> = emptyList(),
    val popularity: Int = 0,
    val rank: Int = 0,
    val relatedManga: List<RelatedAnime> = emptyList(),
    val serialization: List<Serialization> = emptyList(),
    val startDate: String = "",
    val status: String = "",
    val synopsis: String = "",
    val title: String = ""
) {
    data class Author(
        val id: Int = 0,
        val name: String = "",
        val role: String = ""
    )

    data class Serialization(
        val id: Int = 0,
        val name: String = ""
    )
}
