package dev.fizcode.dashboard

import dev.fizcode.dashboard.model.DashboardDestinationItems
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardDestinationItemsTest {

    private val items = listOf(
        DashboardDestinationItems.Anime,
        DashboardDestinationItems.Seasonal,
        DashboardDestinationItems.Manga,
        DashboardDestinationItems.Bookmark
    )

    @Test
    fun `titles are unique and in tab order`() {
        assertEquals(listOf("Anime", "Seasonal", "Manga", "Bookmark"), items.map { it.title })
    }

    @Test
    fun `routes are unique`() {
        assertEquals(items.size, items.map { it.route }.toSet().size)
    }
}
