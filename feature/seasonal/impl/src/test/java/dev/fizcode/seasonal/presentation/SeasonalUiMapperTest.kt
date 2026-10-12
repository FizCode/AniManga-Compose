package dev.fizcode.seasonal.presentation

import dev.fizcode.common.settings.AlternativeTitles
import dev.fizcode.common.settings.TitleStyle
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

    @Test
    fun `titles carry the alternative titles so the chosen style can be shown`() {
        val domain = SeasonalDomainModel.Item(
            id = 1,
            title = "Sen to Chihiro no Kamikakushi",
            alternativeTitles = AlternativeTitles(
                synonyms = listOf("Sen to Chihiro"),
                japanese = "千と千尋の神隠し",
                english = "Spirited Away"
            )
        )

        val titles = mapper.mapToSeasonalUiModel(SeasonalDomainModel(listOf(domain))).single().titles

        assertEquals("Sen to Chihiro no Kamikakushi", titles.resolve(TitleStyle.DEFAULT))
        assertEquals("千と千尋の神隠し", titles.resolve(TitleStyle.JAPANESE))
        assertEquals("Spirited Away", titles.resolve(TitleStyle.ENGLISH))
    }

    @Test
    fun `a media without alternative titles keeps the default title for every style`() {
        val titles = mapper.mapToSeasonalUiModel(SeasonalDomainModel(listOf(item(1, "Bleach"))))
            .single().titles

        TitleStyle.entries.forEach { assertEquals("Bleach", titles.resolve(it)) }
    }
}
