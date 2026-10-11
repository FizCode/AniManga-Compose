package dev.fizcode.mediadetails.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class MediaKindTest {

    @Test
    fun `raw MAL manga types open the manga screen`() {
        listOf("manga", "novel", "light_novel", "one_shot", "doujinshi", "manhwa", "manhua", "oel")
            .forEach { assertEquals(it, MediaKind.MANGA, MediaKind.from(it)) }
    }

    @Test
    fun `bookmark labels open the manga screen`() {
        listOf("Manga", "Light Novel", "One-Shot", "Doujinshi")
            .forEach { assertEquals(it, MediaKind.MANGA, MediaKind.from(it)) }
    }

    @Test
    fun `anime types and unknown values open the anime screen`() {
        listOf("tv", "TV", "ova", "movie", "ona", "", "Unknown")
            .forEach { assertEquals(it, MediaKind.ANIME, MediaKind.from(it)) }
    }
}
