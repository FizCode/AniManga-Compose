package dev.fizcode.ranking.presentation

import dev.fizcode.ranking.domain.model.RankingDomainModel
import dev.fizcode.ranking.presentation.mapper.RankingUiMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class RankingUiMapperTest {

    private val mapper = RankingUiMapper()

    private fun item(id: Int, rank: Int, title: String) = RankingDomainModel.Item(
        id = id,
        rank = rank,
        title = title,
        mean = 8.1,
        studios = listOf("Pierrot", "Bones"),
        genres = listOf("Action", "Fantasy")
    )

    @Test
    fun `rank is kept from the domain model`() {
        val ui = mapper.mapToRankingUiModel(
            RankingDomainModel(listOf(item(1, 1, "A"), item(2, 7, "B")))
        )

        assertEquals(listOf(1, 7), ui.map { it.rank })
        assertEquals(listOf("A", "B"), ui.map { it.title })
    }

    @Test
    fun `rating has two decimals and studios are joined`() {
        val ui = mapper.mapToRankingUiModel(RankingDomainModel(listOf(item(1, 1, "A")))).single()

        assertEquals("8.10", ui.rating)
        assertEquals("Pierrot, Bones", ui.studio)
        assertEquals(listOf("Action", "Fantasy"), ui.genre.toList())
    }
}
