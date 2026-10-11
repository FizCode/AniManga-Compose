package dev.fizcode.mediadetails.domain.model

/** Extra details MyAnimeList does not provide: producers and themes, from AniList. */
internal data class AniListAnimeDetailsDomainModel(
    val producers: List<Link> = emptyList(),
    val themes: List<Link> = emptyList()
) {
    data class Link(
        val name: String = "",
        val url: String = ""
    )
}
