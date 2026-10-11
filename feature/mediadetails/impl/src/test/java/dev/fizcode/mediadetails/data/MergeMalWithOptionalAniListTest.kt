package dev.fizcode.mediadetails.data

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.data.repository.mergeMalWithOptionalAniList
import dev.fizcode.mediadetails.domain.model.AlternativeTitles
import dev.fizcode.mediadetails.domain.model.AniListAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.Broadcast
import dev.fizcode.mediadetails.domain.model.MainPicture
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.StartSeason
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class MergeMalWithOptionalAniListTest {

    private val mal = MalAnimeDetailsDomainModel(
        alternativeTitles = AlternativeTitles(),
        broadcast = Broadcast(),
        mainPicture = MainPicture(),
        startSeason = StartSeason(),
        title = "Bleach"
    )
    private val aniList = AniListAnimeDetailsDomainModel(producers = listOf(AniListAnimeDetailsDomainModel.Link("Studio", "u")))
    private val offline = DomainNetworkState.ErrorNetwork(message = "offline")

    @Test
    fun `both succeed keeps mal and aniList`() {
        val result = mergeMalWithOptionalAniList(
            mal = DomainNetworkState.Success(mal),
            aniList = DomainNetworkState.Success(aniList)
        )

        val data = (result as DomainNetworkState.Success).data
        assertEquals("Bleach", data.malDomainModel.title)
        assertEquals(aniList, data.aniListDomainModel)
    }

    @Test
    fun `aniList failure still succeeds with mal only`() {
        val result = mergeMalWithOptionalAniList(
            mal = DomainNetworkState.Success(mal),
            aniList = offline
        )

        val data = (result as DomainNetworkState.Success).data
        assertEquals("Bleach", data.malDomainModel.title)
        assertNull(data.aniListDomainModel)
    }

    @Test
    fun `aniList empty still succeeds with mal only`() {
        val result = mergeMalWithOptionalAniList(
            mal = DomainNetworkState.Success(mal),
            aniList = DomainNetworkState.Empty
        )

        assertNull((result as DomainNetworkState.Success).data.aniListDomainModel)
    }

    @Test
    fun `mal failure is returned unchanged even when aniList succeeds`() {
        val result = mergeMalWithOptionalAniList(
            mal = offline,
            aniList = DomainNetworkState.Success(aniList)
        )

        assertSame(offline, result)
    }

    @Test
    fun `mal empty stays empty`() {
        val result = mergeMalWithOptionalAniList(
            mal = DomainNetworkState.Empty,
            aniList = DomainNetworkState.Success(aniList)
        )

        assertTrue(result is DomainNetworkState.Empty)
    }
}
