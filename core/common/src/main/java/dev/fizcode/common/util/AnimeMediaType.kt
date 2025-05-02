package dev.fizcode.common.util

/**
 * Convert snake case [mediaType] to be readable "TV, OVA, Movie etc."
 *
 * @param mediaType String, should from [mediaType] response
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

