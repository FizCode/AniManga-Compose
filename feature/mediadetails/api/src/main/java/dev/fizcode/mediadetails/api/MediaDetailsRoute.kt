package dev.fizcode.mediadetails.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class MediaDetailsRoute(
    val mediaType: String,
    val mediaId: Int
) : NavKey
