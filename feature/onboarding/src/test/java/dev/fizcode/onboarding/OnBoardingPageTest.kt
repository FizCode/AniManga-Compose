package dev.fizcode.onboarding

import dev.fizcode.onboarding.presentation.component.OnBoardingPage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OnBoardingPageTest {

    private val pages = listOf(OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third)

    @Test
    fun `every page has a title and description`() {
        assertTrue(pages.all { it.title.isNotBlank() && it.desc.isNotBlank() })
    }

    @Test
    fun `pages are distinct`() {
        assertEquals(3, pages.map { it.title }.toSet().size)
        assertEquals(3, pages.map { it.image }.toSet().size)
    }
}
