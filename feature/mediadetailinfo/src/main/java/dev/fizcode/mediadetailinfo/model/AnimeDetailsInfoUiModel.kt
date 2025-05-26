package dev.fizcode.mediadetailinfo.model

data class AnimeDetailsInfoUiModel(
    val animeDetails: AnimeDetails,
    val animeInfo: AnimeInfo,
    val animeCast: AnimeCast,
    val animeThemes: AnimeThemes
)

data class AnimeDetails(
    val synonym: String = "",
    val japanese: String = "",
    val english: String = "",
    val synopsis: String = "",
    val background: String = ""
)

data class AnimeInfo(
    val type: String = "",
    val episodes: String = "",
    val status: String = "",
    val aired: String = "",
    val premiered: String = "",
    val premieredUrl: String = "",
    val producers: List<AnimeDataWithLink> = emptyList(),
    val licensors: List<AnimeDataWithLink> = emptyList(),
    val studios: List<AnimeDataWithLink> = emptyList(),
    val source: AnimeDataWithLink = AnimeDataWithLink(),
    val genre: List<AnimeDataWithLink> = emptyList(),
    val themes: List<AnimeDataWithLink> = emptyList(),
    val duration: String = "",
    val rating: String = "",
    val score: String = "",
    val ranked: String = "",
    val popularity: String = "",
    val members: String = "",
    val favorites: String = "",
)

data class AnimeDataWithLink(
    val name: String = "",
    val link: String = ""
)

data class AnimeCast(
    val characters: List<AnimeCharacters> = emptyList(),
    val animeStaffs: List<AnimeStaff> = emptyList()
)

data class AnimeCharacters(
    val characterId: Int = 0,
    val character: String = "",
    val characterImage: String = "",
    val characterRole: String = "",
    val characterUrl: String = "",
    val voiceActorId: Int = 0,
    val voiceActorName: String = "",
    val voiceActorImage: String = "",
    val voiceActorLang: String = "",
    val voiceActorUrl: String = ""
)

data class AnimeStaff(
    val staffId: Int = 0,
    val name: String = "",
    val role: String = "",
    val image: String = "",
    val url: String = ""
)

data class AnimeThemes(
    val openingTheme: List<String> = emptyList(),
    val endingTheme: List<String> = emptyList()
)
