package dev.fizcode.common.util

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
 * airingStatus("currently_airing") // returns "Airing"
 * airingStatus("finished_airing")  // returns "Finished"
 * airingStatus("abc")              // returns "Unknown Airing Status"
 * ```
 */
fun airingStatus(status: String): String = when (status) {
    Constant.SNAKE_FINISHED_AIRING -> Constant.FINISHED_AIRING
    Constant.SNAKE_CURRENTLY_AIRING -> Constant.CURRENTLY_AIRING
    Constant.SNAKE_NOT_YET_AIRED -> Constant.NOT_YET_AIRED
    else -> Constant.UNKNOWN_AIRING_STATUS
}
