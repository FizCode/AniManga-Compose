package dev.fizcode.mediadetails.domain.model

/** Extra details MyAnimeList does not provide for manga: favorites and themes, from AniList. */
internal data class AniListMangaDetailsDomainModel(
    val favorites: Int = 0,
    val themes: List<AniListAnimeDetailsDomainModel.Link> = emptyList()
)
