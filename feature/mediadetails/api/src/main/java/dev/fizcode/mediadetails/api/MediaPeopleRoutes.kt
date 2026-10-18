package dev.fizcode.mediadetails.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/** The full list of the characters and voice actors of an anime. */
@Serializable
data class MediaCastRoute(val mediaId: Int) : NavKey

/** The full list of the staff of an anime. */
@Serializable
data class MediaStaffRoute(val mediaId: Int) : NavKey

/** The full list of the characters of a manga. */
@Serializable
data class MangaCharactersRoute(val mediaId: Int) : NavKey
