package dev.fizcode.mediadetails.navigation

import android.net.Uri
import androidx.core.net.toUri
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import java.net.URLEncoder

private const val DETAILS_PATH = "details"
private const val DETAILS_SEGMENT_COUNT = 3

/**
 * Maps `$DEEPLINK_BASE/details/{mediaType}/{mediaId}` to a [MediaDetailsRoute],
 * or returns null when the URI is not a media details link.
 */
fun Uri.toMediaDetailsRouteOrNull(): MediaDetailsRoute? {
    val base = DEEPLINK_BASE.toUri()
    val segments = pathSegments
    if (scheme != base.scheme || host != base.host) return null
    if (segments.size != DETAILS_SEGMENT_COUNT || segments.first() != DETAILS_PATH) return null
    val mediaId = segments[2].toIntOrNull() ?: return null
    return MediaDetailsRoute(mediaType = segments[1], mediaId = mediaId)
}

/**
 * The link that opens the media details screen of [mediaId], the inverse of
 * [toMediaDetailsRouteOrNull]. [mediaType] is percent-encoded as it can contain spaces.
 */
internal fun mediaDetailsDeepLink(mediaType: String, mediaId: Int): String {
    val encodedType = URLEncoder.encode(mediaType, Charsets.UTF_8.name()).replace("+", "%20")
    return "$DEEPLINK_BASE/$DETAILS_PATH/$encodedType/$mediaId"
}
