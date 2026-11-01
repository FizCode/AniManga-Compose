package dev.fizcode.mediadetails.presentation.model

import kotlinx.collections.immutable.ImmutableList

/** The pictures of a media and the one that was tapped, shown in the image bottom sheet. */
data class ImageGallery(
    val urls: ImmutableList<String>,
    val initialIndex: Int = 0
)
