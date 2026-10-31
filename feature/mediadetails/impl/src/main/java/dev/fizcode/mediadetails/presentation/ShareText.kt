package dev.fizcode.mediadetails.presentation

import dev.fizcode.mediadetails.navigation.mediaDetailsDeepLink

/** What the share button sends: the title, the shared [content] and the deep link. */
internal fun buildShareText(
    title: String,
    content: String,
    mediaType: String,
    mediaId: Int
): String = listOf(title.trim(), content.trim(), mediaDetailsDeepLink(mediaType, mediaId))
    .filter { it.isNotEmpty() }
    .joinToString(separator = "\n\n")
