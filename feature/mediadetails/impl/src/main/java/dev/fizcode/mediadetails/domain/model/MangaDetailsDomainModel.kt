package dev.fizcode.mediadetails.domain.model

internal data class MangaDetailsDomainModel(
    val malDomainModel: MalMangaDetailsDomainModel,
    /** Null when AniList could not be reached; the MAL data alone is enough to show the screen. */
    val aniListDomainModel: AniListMangaDetailsDomainModel? = null
)
