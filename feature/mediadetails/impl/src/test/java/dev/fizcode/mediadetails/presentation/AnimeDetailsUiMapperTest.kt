package dev.fizcode.mediadetails.presentation

import dev.fizcode.mediadetails.domain.model.AlternativeTitles
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.Broadcast
import dev.fizcode.mediadetails.domain.model.Genre
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.MainPicture
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.StartSeason
import dev.fizcode.mediadetails.domain.model.Studio
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnimeDetailsUiMapperTest {

    private val mapper = AnimeDetailsUiMapper()

    private fun cast(characterId: Int, role: String) = JikanCastDomainModel(
        character = JikanCastDomainModel.Character(
            images = JikanCastDomainModel.Images(jpg = JikanCastDomainModel.Jpg("char-img")),
            malId = characterId,
            name = "Character $characterId",
            url = "char-url"
        ),
        role = role,
        voiceActors = JikanCastDomainModel.VoiceActor(
            language = "Japanese",
            person = JikanCastDomainModel.Person(
                images = JikanCastDomainModel.PersonImages(
                    JikanCastDomainModel.PersonImagesJpg("va-img")
                ),
                malId = characterId * 100,
                name = "VA $characterId",
                url = "va-url"
            )
        )
    )

    @Test
    fun `cast puts main roles first then sorts by character id`() {
        val result = mapper.mapToAnimeCastUiModel(
            listOf(cast(3, "Supporting"), cast(9, "Main"), cast(1, "Supporting"), cast(5, "Main"))
        )

        assertEquals(listOf(5, 9, 1, 3), result.map { it.characterId })
    }

    @Test
    fun `main role comparison ignores case`() {
        val result = mapper.mapToAnimeCastUiModel(listOf(cast(1, "Supporting"), cast(2, "MAIN")))

        assertEquals(2, result.first().characterId)
    }

    @Test
    fun `cast is limited to ten entries`() {
        val result = mapper.mapToAnimeCastUiModel(List(15) { cast(it, "Main") })

        assertEquals(10, result.size)
    }

    @Test
    fun `cast maps character and voice actor fields`() {
        val ui = mapper.mapToAnimeCastUiModel(listOf(cast(2, "Main"))).single()

        assertEquals("Character 2", ui.character)
        assertEquals("char-img", ui.characterImage)
        assertEquals("Main", ui.characterRole)
        assertEquals("char-url", ui.characterUrl)
        assertEquals(200, ui.voiceActorId)
        assertEquals("VA 2", ui.voiceActorName)
        assertEquals("va-img", ui.voiceActorImage)
        assertEquals("Japanese", ui.voiceActorLang)
        assertEquals("va-url", ui.voiceActorUrl)
    }

    @Test
    fun `empty cast maps to empty list`() {
        assertTrue(mapper.mapToAnimeCastUiModel(emptyList()).isEmpty())
    }

    @Test
    fun `staff joins positions and keeps first ten`() {
        val staff = List(12) {
            JikanStaffDomainModel(
                person = JikanStaffDomainModel.Person(malId = it, name = "Staff $it"),
                positions = listOf("Director", "Script")
            )
        }

        val result = mapper.mapToStaffUiModel(staff)

        assertEquals(10, result.size)
        assertEquals((0..9).toList(), result.map { it.staffId })
        assertEquals("Director, Script", result.first().role)
        assertEquals("Staff 0", result.first().name)
    }

    private val mal = MalAnimeDetailsDomainModel(
        alternativeTitles = AlternativeTitles(
            en = "Bleach EN",
            ja = "ブリーチ",
            synonyms = listOf("BL", "Blch")
        ),
        broadcast = Broadcast(),
        genres = listOf(Genre(id = 1, name = "Action")),
        mainPicture = MainPicture(),
        numFavorites = 4321,
        numListUsers = 1234,
        openingThemes = listOf("#1: \"Opening\" by Artist (eps 1-10)"),
        endingThemes = listOf("#1: \"Ending\" by Artist (eps 1-10)"),
        rating = "pg_13",
        source = "light_novel",
        startDate = "2004-10-05",
        endDate = "2012-03-27",
        startSeason = StartSeason(),
        studios = listOf(Studio(id = 11, name = "Pierrot"))
    )

    @Test
    fun `without jikan the info falls back to mal data`() {
        val ui = mapper.mapToAnimeDetailsUiModel(AnimeDetailsDomainModel(mal, jikanDomainModel = null))

        val info = ui.animeDetailsInfoUiModel.animeInfo
        assertEquals("Oct 5, 2004 to Mar 27, 2012", info.aired)
        assertEquals("Light Novel", info.source.name)
        assertEquals("PG-13 - Teens 13 or older", info.rating)
        assertEquals("1,234", info.members)
        assertEquals("4,321", info.favorites)
        assertEquals(listOf("Action"), info.genre.map { it.name })
        assertEquals("https://myanimelist.net/anime/genre/1", info.genre.single().link)
        assertEquals("https://myanimelist.net/anime/producer/11", info.studios.single().link)
    }

    @Test
    fun `without jikan the jikan only data is empty and flagged unavailable`() {
        val ui = mapper.mapToAnimeDetailsUiModel(AnimeDetailsDomainModel(mal, jikanDomainModel = null))

        val info = ui.animeDetailsInfoUiModel
        assertTrue(info.animeInfo.producers.isEmpty())
        assertTrue(info.animeInfo.licensors.isEmpty())
        assertTrue(info.animeInfo.themes.isEmpty())
        assertEquals(false, info.isExtendedInfoAvailable)
    }

    @Test
    fun `without jikan favorites and songs come from mal`() {
        val ui = mapper.mapToAnimeDetailsUiModel(AnimeDetailsDomainModel(mal, jikanDomainModel = null))

        assertEquals("4,321", ui.animeDetailsHeaderUiModel.favorites)
        assertEquals(listOf("#1: \"Opening\" by Artist (eps 1-10)"), ui.animeDetailsInfoUiModel.animeThemes.openingTheme)
        assertEquals(listOf("#1: \"Ending\" by Artist (eps 1-10)"), ui.animeDetailsInfoUiModel.animeThemes.endingTheme)
    }

    @Test
    fun `without jikan or mal favorites they read as not available`() {
        val ui = mapper.mapToAnimeDetailsUiModel(
            AnimeDetailsDomainModel(mal.copy(numFavorites = 0), jikanDomainModel = null)
        )

        assertEquals("N/A", ui.animeDetailsHeaderUiModel.favorites)
        assertEquals("N/A", ui.animeDetailsInfoUiModel.animeInfo.favorites)
    }

    @Test
    fun `jikan songs win over mal songs`() {
        val jikan = JikanAnimeDetailsDomainModel(
            theme = JikanAnimeDetailsDomainModel.Theme(openings = listOf("jikan opening"))
        )

        val ui = mapper.mapToAnimeDetailsUiModel(AnimeDetailsDomainModel(mal, jikan))

        assertEquals(listOf("jikan opening"), ui.animeDetailsInfoUiModel.animeThemes.openingTheme)
        assertTrue(ui.animeDetailsInfoUiModel.animeThemes.endingTheme.isEmpty())
    }

    @Test
    fun `without jikan the titles come from mal`() {
        val ui = mapper.mapToAnimeDetailsUiModel(AnimeDetailsDomainModel(mal, jikanDomainModel = null))

        val details = ui.animeDetailsInfoUiModel.animeDetails
        assertEquals("Bleach EN", details.english)
        assertEquals("ブリーチ", details.japanese)
        assertEquals("BL, Blch", details.synonym)
    }

    @Test
    fun `with jikan its data is preferred and flagged available`() {
        val jikan = JikanAnimeDetailsDomainModel(
            favorites = 42,
            members = 99,
            rating = "R - 17+",
            source = "Manga",
            titleEnglish = "Jikan EN",
            aired = JikanAnimeDetailsDomainModel.Aired(string = "Jikan aired")
        )

        val ui = mapper.mapToAnimeDetailsUiModel(AnimeDetailsDomainModel(mal, jikan))

        val info = ui.animeDetailsInfoUiModel.animeInfo
        assertEquals("Jikan aired", info.aired)
        assertEquals("Manga", info.source.name)
        assertEquals("R - 17+", info.rating)
        assertEquals("99", info.members)
        assertEquals("42", info.favorites)
        assertEquals("Jikan EN", ui.animeDetailsInfoUiModel.animeDetails.english)
        assertEquals(true, ui.animeDetailsInfoUiModel.isExtendedInfoAvailable)
    }

    @Test
    fun `missing mal dates read as not available`() {
        val ui = mapper.mapToAnimeDetailsUiModel(
            AnimeDetailsDomainModel(mal.copy(startDate = "", endDate = ""), jikanDomainModel = null)
        )

        assertEquals("N/A", ui.animeDetailsInfoUiModel.animeInfo.aired)
    }

    @Test
    fun `mal dates with only a start show an open ended range and partial dates`() {
        val ongoing = mapper.mapToAnimeDetailsUiModel(
            AnimeDetailsDomainModel(mal.copy(startDate = "2024-04", endDate = ""), null)
        )
        val single = mapper.mapToAnimeDetailsUiModel(
            AnimeDetailsDomainModel(mal.copy(startDate = "2016-07-08", endDate = "2016-07-08"), null)
        )

        assertEquals("Apr 2024 to ?", ongoing.animeDetailsInfoUiModel.animeInfo.aired)
        assertEquals("Jul 8, 2016", single.animeDetailsInfoUiModel.animeInfo.aired)
    }
}
