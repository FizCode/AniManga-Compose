package dev.fizcode.mediadetails.domain.model

internal data class AnimeDetailsDomainModel(
    val malDomainModel: MalAnimeDetailsDomainModel,
    /** Null when AniList could not be reached; the MAL data alone is enough to show the screen. */
    val aniListDomainModel: AniListAnimeDetailsDomainModel? = null
)
