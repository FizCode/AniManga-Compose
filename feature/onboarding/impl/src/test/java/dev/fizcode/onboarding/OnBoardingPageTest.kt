package dev.fizcode.onboarding

import dev.fizcode.onboarding.presentation.component.OnBoardingPage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OnBoardingPageTest {

    private val pages = OnBoardingPage.all

    @Test
    fun `pages are shown in order`() {
        assertEquals(
            listOf(
                OnBoardingPage.First,
                OnBoardingPage.Second,
                OnBoardingPage.Third,
                OnBoardingPage.Fourth
            ),
            pages
        )
    }

    @Test
    fun `every page has a title and description`() {
        assertTrue(pages.all { it.title.isNotBlank() && it.desc.isNotBlank() })
    }

    @Test
    fun `pages are distinct`() {
        assertEquals(pages.size, pages.map { it.title }.toSet().size)
        assertEquals(pages.size, pages.map { it.image }.toSet().size)
    }
}
