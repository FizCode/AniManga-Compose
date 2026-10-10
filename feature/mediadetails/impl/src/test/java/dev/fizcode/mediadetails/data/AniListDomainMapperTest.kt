package dev.fizcode.mediadetails.data

import dev.fizcode.mediadetails.data.mapper.AniListDomainMapper
import dev.fizcode.mediadetails.data.response.AniListCastResponse
import dev.fizcode.mediadetails.data.response.AniListPerson
import dev.fizcode.mediadetails.data.response.AniListStaffResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AniListDomainMapperTest {

    private val mapper = AniListDomainMapper()

    private fun person(id: Int, name: String) = AniListPerson(
        id = id,
        siteUrl = "https://anilist.co/$id",
        name = AniListPerson.Name(full = name),
        image = AniListPerson.Image(medium = "img-$id")
    )

    private fun castResponse(vararg edges: AniListCastResponse.Edge) = AniListCastResponse(
        data = AniListCastResponse.Data(
            media = AniListCastResponse.Media(
                characters = AniListCastResponse.Characters(edges = edges.toList())
            )
        )
    )

    private fun staffResponse(vararg edges: AniListStaffResponse.Edge) = AniListStaffResponse(
        data = AniListStaffResponse.Data(
            media = AniListStaffResponse.Media(
                staff = AniListStaffResponse.Staff(edges = edges.toList())
            )
        )
    )

    @Test
    fun `cast maps the character and its japanese voice actor`() {
        val response = castResponse(
            AniListCastResponse.Edge(
                role = "MAIN",
                node = person(1, "Frieren"),
                voiceActors = listOf(person(10, "Atsumi Tanezaki"))
            )
        )

        val cast = mapper.mapToCastDomainModel(response).single()

        assertEquals(1, cast.character.malId)
        assertEquals("Frieren", cast.character.name)
        assertEquals("img-1", cast.character.images.jpg.imageUrl)
        assertEquals("https://anilist.co/1", cast.character.url)
        assertEquals("Japanese", cast.voiceActors.language)
        assertEquals(10, cast.voiceActors.person.malId)
        assertEquals("Atsumi Tanezaki", cast.voiceActors.person.name)
        assertEquals("img-10", cast.voiceActors.person.images.jpg.imageUrl)
        assertEquals("https://anilist.co/10", cast.voiceActors.person.url)
    }

    @Test
    fun `roles are capitalized like jikan so main roles sort first`() {
        val response = castResponse(
            AniListCastResponse.Edge(role = "MAIN", node = person(1, "A")),
            AniListCastResponse.Edge(role = "SUPPORTING", node = person(2, "B")),
            AniListCastResponse.Edge(role = "BACKGROUND", node = person(3, "C")),
            AniListCastResponse.Edge(role = null, node = person(4, "D"))
        )

        val roles = mapper.mapToCastDomainModel(response).map { it.role }

        assertEquals(listOf("Main", "Supporting", "Background", ""), roles)
    }

    @Test
    fun `cast without a voice actor keeps the character with an empty voice actor`() {
        val response = castResponse(
            AniListCastResponse.Edge(role = "MAIN", node = person(1, "Frieren"), voiceActors = emptyList()),
            AniListCastResponse.Edge(role = "MAIN", node = person(2, "Fern"), voiceActors = null)
        )

        val cast = mapper.mapToCastDomainModel(response)

        assertEquals(2, cast.size)
        assertTrue(cast.all { it.voiceActors.person.name.isEmpty() && it.voiceActors.language.isEmpty() })
    }

    @Test
    fun `cast edges without a character are dropped`() {
        val response = castResponse(
            AniListCastResponse.Edge(role = "MAIN", node = null),
            AniListCastResponse.Edge(role = "MAIN", node = person(1, "Frieren"))
        )

        assertEquals(listOf("Frieren"), mapper.mapToCastDomainModel(response).map { it.character.name })
    }

    @Test
    fun `missing media maps to empty lists`() {
        assertTrue(mapper.mapToCastDomainModel(AniListCastResponse()).isEmpty())
        assertTrue(mapper.mapToStaffDomainModel(AniListStaffResponse()).isEmpty())
        assertTrue(
            mapper.mapToCastDomainModel(AniListCastResponse(AniListCastResponse.Data(media = null))).isEmpty()
        )
    }

    @Test
    fun `staff maps the person and keeps the role as a position`() {
        val response = staffResponse(
            AniListStaffResponse.Edge(role = "Director", node = person(5, "Keiichirou Saitou")),
            AniListStaffResponse.Edge(role = " ", node = person(6, "No Role"))
        )

        val staff = mapper.mapToStaffDomainModel(response)

        assertEquals(5, staff[0].person.malId)
        assertEquals("Keiichirou Saitou", staff[0].person.name)
        assertEquals("img-5", staff[0].person.images.jpg.imageUrl)
        assertEquals("https://anilist.co/5", staff[0].person.url)
        assertEquals(listOf("Director"), staff[0].positions)
        assertTrue(staff[1].positions.isEmpty())
    }

    @Test
    fun `staff edges without a person are dropped`() {
        val response = staffResponse(
            AniListStaffResponse.Edge(role = "Director", node = null),
            AniListStaffResponse.Edge(role = "Music", node = person(7, "Composer"))
        )

        assertEquals(listOf("Composer"), mapper.mapToStaffDomainModel(response).map { it.person.name })
    }
}
