package dev.fizcode.common.util

/**
 * Maps a snake_case media type string (typically from the API) to its formatted display name.
 *
 * This function is useful when converting raw media type values from the MyAnimeList API
 * (e.g., "tv", "ova", "movie") into properly capitalized and user-facing text (e.g., "TV", "OVA", "Movie").
 *
 * Supported mappings:
 * - "tv" -> "TV"
 * - "ova" -> "OVA"
 * - "movie" -> "Movie"
 * - "special" -> "Special"
 * - "ona" -> "ONA"
 * - "music" -> "Music"
 * - "tv_special" -> "TV Special"
 *
 * If the input does not match any known media types, returns "Unknown Media Type".
 *
 * @param mediaType The snake_case media type string from the API.
 * @return A human-readable media type string.
 *
 * Example:
 * ```
 * animeMediaType("tv") // returns: "TV"
 * animeMediaType("ona") // returns: "ONA"
 * animeMediaType("-") // returns: "Unknown Media Type"
 * ```
 */
fun animeMediaType(mediaType: String): String = when(mediaType) {
    Constant.SNAKE_TV -> Constant.TV
    Constant.SNAKE_OVA -> Constant.OVA
    Constant.SNAKE_MOVIE -> Constant.MOVIE
    Constant.SNAKE_SPECIAL -> Constant.SPECIAL
    Constant.SNAKE_ONA -> Constant.ONA
    Constant.SNAKE_MUSIC -> Constant.MUSIC
    Constant.SNAKE_TV_SPECIAL -> Constant.TV_SPECIAL
    else -> Constant.UNKNOWN_MEDIA_TYPE
}

