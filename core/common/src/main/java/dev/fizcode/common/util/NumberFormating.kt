package dev.fizcode.common.util

/**
 * Converts an [Int] to a string with comma separators for thousands.
 *
 * This function formats the integer using comma (`,`) as the thousands separator
 * based on the default locale. Useful for displaying large numbers in a
 * more readable form (e.g., 1,000; 10,000; 1,000,000).
 *
 * Examples:
 * - 999 -> "999"
 * - 1_000 -> "1,000"
 * - 123_456 -> "123,456"
 * - 1_000_000 -> "1,000,000"
 *
 * Note: This uses the default system locale, so formatting may vary depending
 * on the device's locale. For consistent US-style formatting, use
 * `String.format(Locale.US, "%,d", this)` instead.
 *
 * @return A string representation of the integer with comma separators.
 */
fun Int.toCommaSeparators(): String = "%,d".format(this)

/**
 * Converts an [Int] into a human-readable string:
 * - Numbers >= 1,000,000 are formatted in millions with one decimal precision and suffixed with "M".
 *   (e.g., 1_234_567 -> "1.2M", 1_000_000 -> "1M")
 * - Numbers < 1,000,000 are formatted with comma separators as thousands.
 *   (e.g., 12_345 -> "12,345", 1_000 -> "1,000", 999 -> "999")
 *
 * Trailing ".0" in million-formatted numbers is removed for cleaner output.
 *
 * Examples:
 * - 999 -> "999"
 * - 1_000 -> "1,000"
 * - 12_345 -> "12,345"
 * - 1_234_567 -> "1.2M"
 * - 1_000_000 -> "1M"
 *
 * @return A formatted string with "M" for millions, or a comma-formatted number if below 1 million.
 */
fun Int.toCommaSeparatorMaxM(): String = when {
    this >= 1_000_000 -> {
        val inMillions = this / 1_000_000.0
        val formatted = "%,.1f".format(inMillions)
        if (formatted.endsWith(".0")) "${formatted.dropLast(2)}M" else "${formatted}M"
    }

    else -> "%,d".format(this)
}
