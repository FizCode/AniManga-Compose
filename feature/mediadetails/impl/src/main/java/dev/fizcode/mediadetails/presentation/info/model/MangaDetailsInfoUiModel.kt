package dev.fizcode.mediadetails.presentation.info.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MangaDetailsInfoUiModel(
    val mangaDetails: AnimeDetails,
    val mangaInfo: MangaInfo,
    /** False when AniList was unreachable, so favorites and themes are missing. */
    val isExtendedInfoAvailable: Boolean = true
)

@Immutable
data class MangaInfo(
    val type: String = "",
    val volumes: String = "",
    val chapters: String = "",
    val status: String = "",
    val published: String = "",
    val authors: ImmutableList<AnimeDataWithLink> = persistentListOf(),
    val serialization: ImmutableList<AnimeDataWithLink> = persistentListOf(),
    val genre: ImmutableList<AnimeDataWithLink> = persistentListOf(),
    val themes: ImmutableList<AnimeDataWithLink> = persistentListOf(),
    val score: String = "",
    val ranked: String = "",
    val popularity: String = "",
    val members: String = "",
    val favorites: String = "",
    val relatedManga: ImmutableList<AnimeRelatedUiModel> = persistentListOf()
)
