package dev.fizcode.common.util.extensions

import dev.fizcode.common.util.Constant

/**
 * Converts an integer (in seconds) into a properly pluralized formatted time string.
 *
 * Only non-zero time units (hours, minutes, seconds) are included.
 * Pluralization is handled (e.g., "1 hour" vs "2 hours").
 *
 * Examples:
 * - 1500 => "25 mins."
 * - 3600 => "1 hour"
 * - 3661 => "1 hour 1 min. 1 sec."
 * - 0    => "0 sec."
 *
 * @receiver Int The duration in seconds.
 * @return A human-readable formatted string.
 */
fun Int.toFormattedTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return listOfNotNull(
        formatUnit(hours, Constant.HOUR, Constant.HOURS).takeIf { hours > 0 },
        formatUnit(minutes, Constant.MIN, Constant.MINS).takeIf { minutes > 0 },
        formatUnit(seconds, Constant.SEC, Constant.SECS).takeIf {
            seconds > 0 || (hours == 0 && minutes == 0)
        }
    ).joinToString(" ")
}

private fun formatUnit(value: Int, singular: String, plural: String): String {
    return "$value ${if (value == 1) singular else plural}"
}
