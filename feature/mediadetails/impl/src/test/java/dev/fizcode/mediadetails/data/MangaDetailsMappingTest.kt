package dev.fizcode.mediadetails.data

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.data.mapper.AniListDomainMapper
import dev.fizcode.mediadetails.data.mapper.MangaDetailsDomainMapper
import dev.fizcode.mediadetails.data.repository.mergeMalMangaWithOptionalAniList
import dev.fizcode.mediadetails.data.response.AniListDetailsResponse
import dev.fizcode.mediadetails.data.response.MalMangaDetailsResponse
import dev.fizcode.mediadetails.domain.model.AniListMangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.MalMangaDetailsDomainModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MangaDetailsMappingTest {

    @Test
    fun `maps authors and serialization and skips blank ones`() {
        val domain = MangaDetailsDomainMapper().mapToMalMangaDetailsModel(
            MalMangaDetailsResponse(
                authors = listOf(
                    MalMangaDetailsResponse.Author(
                        node = MalMangaDetailsResponse.AuthorNode(id = 5, firstName = "Tite", lastName = "Kubo"),
                        role = "Story & Art"
                    ),
                    MalMangaDetailsResponse.Author(node = MalMangaDetailsResponse.AuthorNode(id = 6)),
                    MalMangaDetailsResponse.Author(node = null)
                ),
                serialization = listOf(
                    MalMangaDetailsResponse.Serialization(
                        MalMangaDetailsResponse.SerializationNode(id = 83, name = "Shounen Jump")
                    ),
                    MalMangaDetailsResponse.Serialization(MalMangaDetailsResponse.SerializationNode(id = 1))
                )
            )
        )

        assertEquals(listOf("Tite Kubo"), domain.authors.map { it.name })
        assertEquals("Story & Art", domain.authors.single().role)
        assertEquals(listOf("Shounen Jump"), domain.serialization.map { it.name })
    }

    @Test
    fun `empty response maps to defaults`() {
        val domain = MangaDetailsDomainMapper().mapToMalMangaDetailsModel(MalMangaDetailsResponse())

        assertEquals(0, domain.numVolumes)
        assertTrue(domain.genres.isEmpty())
        assertEquals("", domain.title)
    }

    @Test
    fun `AniList manga details keep favorites and non spoiler tags with manga links`() {
        val domain = AniListDomainMapper().mapToMangaDetailsDomainModel(
            AniListDetailsResponse(
                data = AniListDetailsResponse.Data(
                    AniListDetailsResponse.Media(
                        favourites = 77,
                        tags = listOf(
                            AniListDetailsResponse.Tag("Time Skip", isMediaSpoiler = false),
                            AniListDetailsResponse.Tag("Twist", isMediaSpoiler = true)
                        )
                    )
                )
            )
        )

        assertEquals(77, domain.favorites)
        assertEquals(listOf("Time Skip"), domain.themes.map { it.name })
        assertEquals("https://anilist.co/search/manga?includedTags=Time+Skip", domain.themes.single().url)
    }

    @Test
    fun `merge keeps MAL data when AniList fails`() {
        val mal = MalMangaDetailsDomainModel(title = "Bleach")

        val result = mergeMalMangaWithOptionalAniList(
            mal = DomainNetworkState.Success(mal),
            aniList = DomainNetworkState.ErrorNetwork(message = "down")
        )

        val data = (result as DomainNetworkState.Success).data
        assertEquals("Bleach", data.malDomainModel.title)
        assertNull(data.aniListDomainModel)
    }

    @Test
    fun `merge returns the MAL error as is`() {
        val error = DomainNetworkState.ErrorNetwork(message = "mal down")

        val result = mergeMalMangaWithOptionalAniList(
            mal = error,
            aniList = DomainNetworkState.Success(AniListMangaDetailsDomainModel())
        )

        assertEquals(error, result)
    }
}
