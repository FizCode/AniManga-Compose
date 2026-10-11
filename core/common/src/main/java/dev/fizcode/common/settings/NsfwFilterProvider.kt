package dev.fizcode.common.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/** The NSFW level the user picked in the settings. */
interface NsfwFilterProvider {
    val nsfwFilter: Flow<NsfwFilter>
}

suspend fun NsfwFilterProvider.current(): NsfwFilter = nsfwFilter.first()

/** Emits every time the user picks another level, but not for the one picked at the start. */
fun NsfwFilterProvider.changes(): Flow<Unit> = nsfwFilter.distinctUntilChanged().drop(1).map { }

/** MyAnimeList leaves NSFW media out of a list unless it is asked for them. */
val NsfwFilter.includesNsfw: Boolean
    get() = this != NsfwFilter.SAFE

/**
 * Whether a media with the MyAnimeList content [rating] (`white`, `gray` or `black`) may be shown.
 * A media without a rating is treated as safe.
 */
fun NsfwFilter.allows(rating: String?): Boolean {
    val level = when (rating) {
        "gray" -> 1
        "black" -> 2
        else -> 0
    }
    val allowed = when (this) {
        NsfwFilter.SAFE -> 0
        NsfwFilter.GREY -> 1
        NsfwFilter.NOT_SAFE -> 2
    }
    return level <= allowed
}
