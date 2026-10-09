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
    fun `domain model maps to entity with comma separated genres`() {
        val entity = BookmarkDomainMapper().mapTopBookmarkEntity(
            AnimeBookmarkUiMapper().mapToBookmarkDomainModel(argument)
        )

        assertEquals(10, entity.mediaId)
        assertEquals("TV", entity.mediaType)
        assertEquals("poster", entity.posterPath)
        assertEquals("8.5", entity.rating)
        assertEquals("Bleach", entity.title)
        assertEquals("12 Episodes", entity.episodes)
        assertEquals("Finished", entity.status)
        assertEquals("Pierrot", entity.studio)
        assertEquals("Action, Comedy", entity.genres)
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
