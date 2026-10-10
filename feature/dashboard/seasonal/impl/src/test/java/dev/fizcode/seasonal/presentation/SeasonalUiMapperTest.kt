package dev.fizcode.seasonal.presentation

import dev.fizcode.seasonal.domain.model.SeasonalDomainModel
import dev.fizcode.seasonal.presentation.mapper.SeasonalUiMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class SeasonalUiMapperTest {

    private val mapper = SeasonalUiMapper()

    private fun item(id: Int, title: String) = SeasonalDomainModel.Item(
        id = id,
        title = title,
        mean = 8.1,
        studios = listOf("Pierrot", "Bones"),
        genres = listOf("Action", "Fantasy")
    )

    @Test
    fun `rank follows the position in the list`() {
        val ui = mapper.mapToSeasonalUiModel(SeasonalDomainModel(listOf(item(1, "A"), item(2, "B"))))

        assertEquals(listOf(1, 2), ui.map { it.rank })
        assertEquals(listOf("A", "B"), ui.map { it.title })
    }

    @Test
    fun `rating has two decimals and studios are joined`() {
        val ui = mapper.mapToSeasonalUiModel(SeasonalDomainModel(listOf(item(1, "A")))).single()

        assertEquals("8.10", ui.rating)
        assertEquals("Pierrot, Bones", ui.studio)
        assertEquals(listOf("Action", "Fantasy"), ui.genre.toList())
    }
}
