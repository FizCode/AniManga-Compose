package dev.fizcode.mediadetails.presentation

import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
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
}
