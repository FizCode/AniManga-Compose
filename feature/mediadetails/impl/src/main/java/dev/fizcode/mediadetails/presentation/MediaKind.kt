package dev.fizcode.mediadetails.presentation

/** Which details screen a media type opens: anime titles and manga-like titles differ. */
internal enum class MediaKind {
    ANIME,
    MANGA;

    companion object {
        private val MANGA_TYPES = setOf(
            "manga", "novel", "light_novel", "one_shot", "doujinshi", "manhwa", "manhua", "oel"
        )

        /**
         * Accepts the raw MAL type (`one_shot`) and the label stored with bookmarks (`One-Shot`),
         * which is why the value is normalized first.
         */
        fun from(mediaType: String): MediaKind {
            val normalized = mediaType.trim().lowercase().replace('-', '_').replace(' ', '_')
            return if (normalized in MANGA_TYPES) MANGA else ANIME
        }
    }
}
