package dev.fizcode.mediadetails.presentation

import dev.fizcode.mediadetails.navigation.DEEPLINK_BASE
import java.net.URLEncoder

private const val DETAILS_PATH = "details"

/**
 * The link that opens the media details screen of [mediaId], the inverse of
 * `Uri.toMediaDetailsRouteOrNull()`. [mediaType] is percent-encoded as it can contain spaces.
 */
internal fun mediaDetailsDeepLink(mediaType: String, mediaId: Int): String {
    val encodedType = URLEncoder.encode(mediaType, Charsets.UTF_8.name()).replace("+", "%20")
    return "$DEEPLINK_BASE/$DETAILS_PATH/$encodedType/$mediaId"
}

/** What the share button sends: the title, the shared [content] and the deep link. */
internal fun buildShareText(
    title: String,
    content: String,
    mediaType: String,
    mediaId: Int
): String = listOf(title.trim(), content.trim(), mediaDetailsDeepLink(mediaType, mediaId))
    .filter { it.isNotEmpty() }
    .joinToString(separator = "\n\n")
