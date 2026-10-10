package dev.fizcode.mediadetails.navigation

import android.net.Uri
import androidx.core.net.toUri
import dev.fizcode.common.util.DEEPLINK_BASE
import dev.fizcode.mediadetails.api.MediaDetailsRoute

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
