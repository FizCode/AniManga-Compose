package dev.fizcode.mediadetails.presentation.info.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class AnimeDetailsInfoUiModel(
    val animeDetails: AnimeDetails,
    val animeInfo: AnimeInfo,
    val animeThemes: AnimeThemes,
    /** False when Jikan was unreachable, so producers, licensors and themes are missing. */
    val isExtendedInfoAvailable: Boolean = true
)

@Immutable
data class AnimeDetails(
    val synonym: String = "",
    val japanese: String = "",
    val english: String = "",
    val synopsis: String = "",
    val background: String = ""
)

@Immutable
data class AnimeInfo(
    val type: String = "",
    val episodes: String = "",
    val status: String = "",
    val aired: String = "",
    val premiered: String = "",
    val premieredUrl: String = "",
    val producers: ImmutableList<AnimeDataWithLink> = persistentListOf(),
    val licensors: ImmutableList<AnimeDataWithLink> = persistentListOf(),
    val studios: ImmutableList<AnimeDataWithLink> = persistentListOf(),
    val source: AnimeDataWithLink = AnimeDataWithLink(),
    val genre: ImmutableList<AnimeDataWithLink> = persistentListOf(),
    val themes: ImmutableList<AnimeDataWithLink> = persistentListOf(),
    val duration: String = "",
    val rating: String = "",
    val score: String = "",
    val ranked: String = "",
    val popularity: String = "",
    val members: String = "",
    val favorites: String = "",
    val relatedAnime: ImmutableList<AnimeRelatedUiModel> = persistentListOf()
)

@Immutable
data class AnimeDataWithLink(
    val name: String = "",
    val link: String = ""
)

@Immutable
data class AnimeThemes(
    val openingTheme: ImmutableList<String> = persistentListOf(),
    val endingTheme: ImmutableList<String> = persistentListOf()
)
