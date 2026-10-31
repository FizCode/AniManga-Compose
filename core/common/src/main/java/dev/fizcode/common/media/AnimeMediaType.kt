package dev.fizcode.common.media

enum class AnimeMediaType(val apiValue: String, val label: String) {
    TV("tv", "TV"),
    OVA("ova", "OVA"),
    MOVIE("movie", "Movie"),
    SPECIAL("special", "Special"),
    ONA("ona", "ONA"),
    MUSIC("music", "Music"),
    TV_SPECIAL("tv_special", "TV Special"),
    UNKNOWN("", "Unknown Media Type");

    companion object {
        fun fromApi(value: String): AnimeMediaType = entries.firstOrNull { it.apiValue == value } ?: UNKNOWN
    }
}
