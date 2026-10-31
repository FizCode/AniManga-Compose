package dev.fizcode.mediadetails.presentation

import dev.fizcode.mediadetails.data.mapper.BookmarkDomainMapper
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.presentation.mapper.AnimeBookmarkMapper.mapToBookmarkArgs
import dev.fizcode.mediadetails.presentation.mapper.AnimeBookmarkUiMapper
import dev.fizcode.mediadetails.presentation.header.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetailsInfoUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetails
import dev.fizcode.mediadetails.presentation.info.model.AnimeInfo
import dev.fizcode.mediadetails.presentation.info.model.AnimeThemes
import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import dev.fizcode.mediadetails.presentation.model.BookmarkArgument
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import dev.fizcode.common.settings.AlternativeTitles
import dev.fizcode.common.settings.MediaTitles
import org.junit.Test

class BookmarkMappersTest {

    private val argument = BookmarkArgument(
        mediaId = 10,
        mediaType = "TV",
        posterPath = "poster",
        rating = "8.5",
        title = "Bleach",
        episodes = "12 Episodes",
        status = "Finished",
        studio = "Pierrot",
        genres = listOf("Action", "Comedy")
    )

    @Test
    fun `argument maps to domain model`() {
        val domain = AnimeBookmarkUiMapper().mapToBookmarkDomainModel(argument)

        assertEquals(
            BookmarkDomainModel(
                mediaId = 10,
                mediaType = "TV",
                posterPath = "poster",
                rating = "8.5",
                title = "Bleach",
                episodes = "12 Episodes",
                status = "Finished",
                studio = "Pierrot",
                genres = listOf("Action", "Comedy")
            ),
            domain
        )
    }

    @Test
    fun `domain model maps to record with its genres`() {
        val record = BookmarkDomainMapper().mapToBookmarkRecord(
            AnimeBookmarkUiMapper().mapToBookmarkDomainModel(argument)
        )

        assertEquals(10, record.mediaId)
        assertEquals("TV", record.mediaType)
        assertEquals("poster", record.posterPath)
        assertEquals("8.5", record.rating)
        assertEquals("Bleach", record.title)
        assertEquals("12 Episodes", record.episodes)
        assertEquals("Finished", record.status)
        assertEquals("Pierrot", record.studio)
        assertEquals(listOf("Action", "Comedy"), record.genres)
    }

    @Test
    fun `alternative titles are saved with the bookmark`() {
        val titles = MediaTitles(
            default = "Bleach",
            alternative = AlternativeTitles(
                synonyms = listOf("Burichi", "Blich"),
                japanese = "ブリーチ",
                english = "Bleach EN"
            )
        )

        val record = BookmarkDomainMapper().mapToBookmarkRecord(
            AnimeBookmarkUiMapper().mapToBookmarkDomainModel(argument.copy(titles = titles))
        )

        assertEquals(titles.alternative, record.alternativeTitles)
    }

    @Test
    fun `details ui model maps to bookmark argument`() {
        val details = AnimeDetailsUiModel(
            animeDetailsHeaderUiModel = AnimeDetailsHeaderUiModel(
                pictures = persistentListOf(),
                largePicture = persistentListOf(),
                posterPath = "poster",
                title = "Bleach",
                mediaType = "TV",
                releaseSeason = "",
                studio = "Pierrot",
                releaseInfo = "",
                duration = "",
                rank = "",
                popularity = "",
                members = "",
                favorites = "",
                score = "8.5",
                stars = 8.5,
                totalVote = "",
                genre = persistentListOf("Action", "Comedy")
            ),
            animeDetailsInfoUiModel = AnimeDetailsInfoUiModel(
                animeDetails = AnimeDetails(),
                animeInfo = AnimeInfo(episodes = "12 Episodes", status = "Finished"),
                animeThemes = AnimeThemes()
            )
        )

        assertEquals(argument, details.mapToBookmarkArgs(mediaId = 10))
    }
}
