package dev.fizcode.mediadetails.presentation

import dev.fizcode.mediadetails.domain.model.AlternativeTitles
import dev.fizcode.mediadetails.domain.model.AniListAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.AniListMangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.Genre
import dev.fizcode.mediadetails.domain.model.MalMangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.MangaDetailsDomainModel
import dev.fizcode.mediadetails.presentation.mapper.MangaBookmarkMapper.mapToBookmarkArgs
import dev.fizcode.mediadetails.presentation.mapper.MangaDetailsUiMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MangaDetailsUiMapperTest {

    private val mapper = MangaDetailsUiMapper()

    private val mal = MalMangaDetailsDomainModel(
        alternativeTitles = AlternativeTitles(en = "Bleach", ja = "ブリーチ", synonyms = listOf("B")),
        authors = listOf(MalMangaDetailsDomainModel.Author(id = 1, name = "Tite Kubo", role = "Story & Art")),
        title = "Bleach",
        mediaType = "manga",
        status = "finished",
        numVolumes = 74,
        numChapters = 705,
        startDate = "2001-08-07",
        endDate = "2016-08-22",
        serialization = listOf(MalMangaDetailsDomainModel.Serialization(id = 83, name = "Shounen Jump (Weekly)")),
        genres = listOf(Genre(id = 1, name = "Action")),
        mean = 8.48,
        numScoringUsers = 324,
        rank = 2,
        popularity = 3,
        numListUsers = 1234
    )

    private val aniList = AniListMangaDetailsDomainModel(
        favorites = 2120,
        themes = listOf(AniListAnimeDetailsDomainModel.Link(name = "Shinigami", url = "url"))
    )

    @Test
    fun `header shows volumes, chapters, status and published dates`() {
        val header = mapper.mapToMangaDetailsUiModel(MangaDetailsDomainModel(mal, aniList))
            .mangaDetailsHeaderUiModel

        assertEquals("Manga", header.mediaType)
        assertEquals("Shounen Jump (Weekly)", header.serialization)
        assertEquals("Vol. 74 Ch. 705 | Finished", header.publishInfo)
        assertEquals("Aug 7, 2001 to Aug 22, 2016", header.publishedDates)
        assertEquals("#2", header.rank)
        assertEquals("#3", header.popularity)
    }

    @Test
    fun `unknown counts are shown as question marks`() {
        val header = mapper.mapToMangaDetailsUiModel(
            MangaDetailsDomainModel(mal.copy(numVolumes = 0, numChapters = 0, status = "currently_publishing"))
        ).mangaDetailsHeaderUiModel

        assertEquals("Vol. ?? Ch. ?? | Publishing", header.publishInfo)
    }

    @Test
    fun `favorites and themes come from AniList`() {
        val ui = mapper.mapToMangaDetailsUiModel(MangaDetailsDomainModel(mal, aniList))

        assertEquals("Shinigami", ui.mangaDetailsInfoUiModel.mangaInfo.themes.single().name)
        assertEquals("2,120", ui.mangaDetailsInfoUiModel.mangaInfo.favorites)
        assertTrue(ui.mangaDetailsInfoUiModel.isExtendedInfoAvailable)
    }

    @Test
    fun `missing AniList data flags the extras as unavailable`() {
        val ui = mapper.mapToMangaDetailsUiModel(MangaDetailsDomainModel(mal, aniListDomainModel = null))

        assertEquals("N/A", ui.mangaDetailsInfoUiModel.mangaInfo.favorites)
        assertTrue(ui.mangaDetailsInfoUiModel.mangaInfo.themes.isEmpty())
        assertEquals(false, ui.mangaDetailsInfoUiModel.isExtendedInfoAvailable)
    }

    @Test
    fun `info links authors, magazine and genres to MyAnimeList`() {
        val info = mapper.mapToMangaDetailsUiModel(MangaDetailsDomainModel(mal, aniList))
            .mangaDetailsInfoUiModel.mangaInfo

        assertEquals("Tite Kubo", info.authors.single().name)
        assertEquals("https://myanimelist.net/people/1", info.authors.single().link)
        assertEquals("https://myanimelist.net/manga/magazine/83", info.serialization.single().link)
        assertEquals("https://myanimelist.net/manga/genre/1", info.genre.single().link)
    }

    @Test
    fun `bookmark keeps volumes and chapters, status and magazine`() {
        val args = mapper.mapToMangaDetailsUiModel(MangaDetailsDomainModel(mal, aniList))
            .mapToBookmarkArgs(mediaId = 9)

        assertEquals(9, args.mediaId)
        assertEquals("Manga", args.mediaType)
        assertEquals("Vol. 74 Ch. 705", args.episodes)
        assertEquals("Finished", args.status)
        assertEquals("Shounen Jump (Weekly)", args.studio)
    }

    @Test
    fun `characters list main roles first`() {
        fun cast(id: Int, role: String) = CastDomainModel(
            character = CastDomainModel.Character(id = id, name = "C$id"),
            role = role
        )

        val characters = mapper.mapToCharacterUiModel(
            listOf(cast(1, "Supporting"), cast(3, "Main"), cast(2, "Main"))
        )

        assertEquals(listOf(2, 3, 1), characters.map { it.characterId })
    }

    @Test
    fun `a page of the characters keeps every entry in the order it came`() {
        fun cast(id: Int, role: String) = CastDomainModel(
            character = CastDomainModel.Character(id = id, name = "C$id"),
            role = role
        )

        val characters = mapper.mapToCharacterPage(
            listOf(cast(1, "Supporting"), cast(3, "Main")) + List(30) { cast(it + 10, "Main") }
        )

        assertEquals(32, characters.size)
        assertEquals(listOf(1, 3), characters.take(2).map { it.characterId })
    }
}
