package dev.fizcode.mediadetails.presentation

import dev.fizcode.mediadetails.presentation.header.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetails.presentation.header.model.MangaDetailsHeaderUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeCastUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeDataWithLink
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetails
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetailsInfoUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeInfo
import dev.fizcode.mediadetails.presentation.info.model.AnimeRelatedUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeThemes
import dev.fizcode.mediadetails.presentation.info.model.MangaCharacterUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaDetailsInfoUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaInfo
import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import dev.fizcode.mediadetails.presentation.model.MangaDetailsUiModel
import kotlinx.collections.immutable.persistentListOf

/** Sample data used only by `@Preview` functions. */
internal object MediaDetailsPreviewData {

    private const val SYNOPSIS = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
        "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris."

    private fun link(name: String) = AnimeDataWithLink(name = name, link = "https://example.com")

    val anime = AnimeDetailsUiModel(
        animeDetailsHeaderUiModel = AnimeDetailsHeaderUiModel(
            pictures = persistentListOf("https://example.com/1.jpg", "https://example.com/2.jpg"),
            largePicture = persistentListOf("https://example.com/1l.jpg"),
            posterPath = "https://example.com/poster.jpg",
            title = "Sample Anime Title",
            mediaType = "TV",
            releaseSeason = "Spring 2024",
            studio = "Sample Studio",
            releaseInfo = "12 eps | Finished",
            duration = "24 min",
            rank = "#12",
            popularity = "#34",
            members = "1.2M",
            favorites = "56K",
            score = "8.75",
            stars = 4.4,
            totalVote = "350K",
            genre = persistentListOf("Action", "Adventure", "Fantasy")
        ),
        animeDetailsInfoUiModel = AnimeDetailsInfoUiModel(
            animeDetails = AnimeDetails(
                synonym = "Sample Alt Title",
                japanese = "サンプル",
                english = "Sample Anime Title",
                synopsis = SYNOPSIS,
                background = SYNOPSIS
            ),
            animeInfo = AnimeInfo(
                type = "TV",
                episodes = "12",
                status = "Finished Airing",
                aired = "Apr 5, 2024 to Jun 21, 2024",
                premiered = "Spring 2024",
                producers = persistentListOf(link("Sample Producer")),
                studios = persistentListOf(link("Sample Studio")),
                source = link("Manga"),
                genre = persistentListOf(link("Action"), link("Adventure")),
                themes = persistentListOf(link("Isekai")),
                duration = "24 min per ep",
                rating = "PG-13",
                score = "8.75",
                ranked = "#12",
                popularity = "#34",
                members = "1,200,000",
                favorites = "56,000",
                relatedAnime = persistentListOf(
                    AnimeRelatedUiModel(1, "Sample Anime Season 2", "Sequel", "https://example.com")
                )
            ),
            animeThemes = AnimeThemes(
                openingTheme = persistentListOf("1: \"Opening Song\" by Artist (eps 1-12)"),
                endingTheme = persistentListOf("1: \"Ending Song\" by Artist (eps 1-12)")
            )
        )
    )

    val animeCast = persistentListOf(
        AnimeCastUiModel(
            characterId = 1,
            character = "Sample Hero",
            characterRole = "Main",
            voiceActorId = 1,
            voiceActorName = "Sample Actor",
            voiceActorLang = "Japanese"
        ),
        AnimeCastUiModel(
            characterId = 2,
            character = "Sample Rival",
            characterRole = "Supporting",
            voiceActorId = 2,
            voiceActorName = "Another Actor",
            voiceActorLang = "Japanese"
        )
    )

    val animeStaff = persistentListOf(
        AnimeStaffUiModel(staffId = 1, name = "Sample Director", role = "Director"),
        AnimeStaffUiModel(staffId = 2, name = "Sample Composer", role = "Music")
    )

    val manga = MangaDetailsUiModel(
        mangaDetailsHeaderUiModel = MangaDetailsHeaderUiModel(
            pictures = persistentListOf("https://example.com/1.jpg"),
            largePicture = persistentListOf("https://example.com/1l.jpg"),
            posterPath = "https://example.com/poster.jpg",
            title = "Sample Manga Title",
            mediaType = "Manga",
            serialization = "Sample Magazine",
            publishInfo = "Vol. 20 Ch. 180 | Finished",
            publishedDates = "Aug 7, 2010 to Aug 22, 2016",
            rank = "#8",
            popularity = "#20",
            members = "900K",
            favorites = "48K",
            score = "9.01",
            stars = 4.5,
            totalVote = "280K",
            genre = persistentListOf("Drama", "Mystery")
        ),
        mangaDetailsInfoUiModel = MangaDetailsInfoUiModel(
            mangaDetails = AnimeDetails(
                synonym = "Sample Alt Title",
                japanese = "サンプル",
                english = "Sample Manga Title",
                synopsis = SYNOPSIS,
                background = SYNOPSIS
            ),
            mangaInfo = MangaInfo(
                type = "Manga",
                volumes = "20",
                chapters = "180",
                status = "Finished",
                published = "Aug 7, 2010 to Aug 22, 2016",
                authors = persistentListOf(link("Sample Author (Story & Art)")),
                serialization = persistentListOf(link("Sample Magazine")),
                genre = persistentListOf(link("Drama"), link("Mystery")),
                themes = persistentListOf(link("Psychological")),
                score = "9.01",
                ranked = "#8",
                popularity = "#20",
                members = "900,000",
                favorites = "48,000",
                relatedManga = persistentListOf(
                    AnimeRelatedUiModel(1, "Sample Manga Side Story", "Side story", "https://example.com")
                )
            )
        )
    )

    val mangaCharacters = persistentListOf(
        MangaCharacterUiModel(characterId = 1, name = "Sample Hero", role = "Main"),
        MangaCharacterUiModel(characterId = 2, name = "Sample Rival", role = "Supporting")
    )
}
