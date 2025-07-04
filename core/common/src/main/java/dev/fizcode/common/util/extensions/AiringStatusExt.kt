package dev.fizcode.common.util.extensions

import dev.fizcode.common.util.InternalConstant

/**
 * Converts a snake_case airing status string from the MyAnimeList API
 * into a human-readable format suitable for UI display.
 *
 * This is useful for transforming status identifiers like "currently_airing" or "not_yet_aired"
 * into proper display strings such as "Airing" or "Not Yet Aired".
 *
 * @param status The raw snake_case airing status string from the API.
 *               Expected values include: "finished_airing", "currently_airing", "not_yet_aired".
 * @return A formatted airing status string (e.g., "Airing", "Finished"),
 *         or "Unknown Airing Status" if the input is unrecognized.
 *
 * Example:
 * ```
 * airingStatus("currently_airing") // returns "Currently Airing"
 * airingStatus("finished_airing")  // returns "Finished"
 * airingStatus("abc")              // returns "Unknown Airing Status"
 * ```
 */
fun airingStatus(status: String): String = when (status) {
    InternalConstant.SNAKE_FINISHED_AIRING -> InternalConstant.FINISHED_AIRING
    InternalConstant.SNAKE_CURRENTLY_AIRING -> InternalConstant.CURRENTLY_AIRING
    InternalConstant.SNAKE_NOT_YET_AIRED -> InternalConstant.NOT_YET_AIRED
    else -> InternalConstant.UNKNOWN_AIRING_STATUS
}
