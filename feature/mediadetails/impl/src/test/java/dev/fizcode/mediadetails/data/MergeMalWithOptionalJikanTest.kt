package dev.fizcode.mediadetails.data

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mediadetails.data.repository.mergeMalWithOptionalJikan
import dev.fizcode.mediadetails.domain.model.AlternativeTitles
import dev.fizcode.mediadetails.domain.model.Broadcast
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.MainPicture
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.StartSeason
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class MergeMalWithOptionalJikanTest {

    private val mal = MalAnimeDetailsDomainModel(
        alternativeTitles = AlternativeTitles(),
        broadcast = Broadcast(),
        mainPicture = MainPicture(),
        startSeason = StartSeason(),
        title = "Bleach"
    )
    private val jikan = JikanAnimeDetailsDomainModel(favorites = 10)
    private val offline = DomainNetworkState.ErrorNetwork(message = "offline")

    @Test
    fun `both succeed keeps mal and jikan`() {
        val result = mergeMalWithOptionalJikan(
            mal = DomainNetworkState.Success(mal),
            jikan = DomainNetworkState.Success(jikan)
        )

        val data = (result as DomainNetworkState.Success).data
        assertEquals("Bleach", data.malDomainModel.title)
        assertEquals(jikan, data.jikanDomainModel)
    }

    @Test
    fun `jikan failure still succeeds with mal only`() {
        val result = mergeMalWithOptionalJikan(
            mal = DomainNetworkState.Success(mal),
            jikan = offline
        )

        val data = (result as DomainNetworkState.Success).data
        assertEquals("Bleach", data.malDomainModel.title)
        assertNull(data.jikanDomainModel)
    }

    @Test
    fun `jikan empty still succeeds with mal only`() {
        val result = mergeMalWithOptionalJikan(
            mal = DomainNetworkState.Success(mal),
            jikan = DomainNetworkState.Empty
        )

        assertNull((result as DomainNetworkState.Success).data.jikanDomainModel)
    }

    @Test
    fun `mal failure is returned unchanged even when jikan succeeds`() {
        val result = mergeMalWithOptionalJikan(
            mal = offline,
            jikan = DomainNetworkState.Success(jikan)
        )

        assertSame(offline, result)
    }

    @Test
    fun `mal empty stays empty`() {
        val result = mergeMalWithOptionalJikan(
            mal = DomainNetworkState.Empty,
            jikan = DomainNetworkState.Success(jikan)
        )

        assertTrue(result is DomainNetworkState.Empty)
    }
}
