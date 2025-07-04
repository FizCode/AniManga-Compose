package dev.fizcode.common.util.extensions

/**
 * Converts an [Int] to a string with comma separators for thousands.
 *
 * This function formats the integer using a comma (`,`) as the thousands separator.
 * Optionally, it can return `"N/A"` if the value is zero and `zeroIsNa` is set to `true`.
 * Useful for displaying large numbers in a more readable format (e.g., 1,000; 10,000; 1,000,000).
 *
 * Examples:
 * - `999.toCommaSeparators()` -> "999"
 * - `1_000.toCommaSeparators()` -> "1,000"
 * - `123_456.toCommaSeparators()` -> "123,456"
 * - `1_000_000.toCommaSeparators()` -> "1,000,000"
 * - `0.toCommaSeparators(zeroIsNa = true)` -> "N/A"
 *
 * Note: This uses the default system locale, so formatting may vary depending
 * on the device's locale. For consistent US-style formatting, use:
 * `String.format(Locale.US, "%,d", this)`
 *
 * @param zeroIsNa If `true`, returns "N/A" when the value is zero. Default is `false`.
 * @return A string representation of the integer with comma separators or "N/A".
 */
fun Int.toCommaSeparators(zeroIsNa: Boolean = false): String = if (zeroIsNa && this == 0) {
    "N/A"
} else {
    "%,d".format(this)
}

/**
 * Converts an [Int] into a human-readable compact number format.
 *
 * Formatting rules:
 * - Values >= 1,000,000 are displayed in millions with one decimal precision and suffixed with "M".
 *   Example: 1_234_567 -> "1.2M", 1_000_000 -> "1M"
 * - Values >= 10,000 are displayed in whole thousands and suffixed with "K".
 *   Example: 12_345 -> "12K", 123_456 -> "123K"
 * - Values < 10,000 are formatted with comma separators.
 *   Example: 1_000 -> "1,000", 999 -> "999"
 *
 * If `zeroIsNa` is `true` and the value is zero, this function returns `"N/A"`.
 *
 * Trailing ".0" in million-formatted numbers is removed for cleaner output.
 *
 * Examples:
 * - `999.toCompactNumber()` -> "999"
 * - `1_000.toCompactNumber()` -> "1,000"
 * - `12_345.toCompactNumber()` -> "12K"
 * - `1_234_567.toCompactNumber()` -> "1.2M"
 * - `1_000_000.toCompactNumber()` -> "1M"
 * - `0.toCompactNumber(zeroIsNa = true)` -> "N/A"
 *
 * @param zeroIsNa If `true`, returns "N/A" when the value is zero. Default is `false`.
 * @return A formatted compact string representation with "M", "K", or comma separators.
 */
fun Int.toCompactNumber(zeroIsNa: Boolean = false): String = when {
    this >= 1_000_000 -> {
        val inMillions = this / 1_000_000.0
        val formatted = "%,.1f".format(inMillions)
        if (formatted.endsWith(".0")) "${formatted.dropLast(2)}M" else "${formatted}M"
    }

    this >= 10_000 -> {
        val inThousands = this / 1_000
        "${inThousands}K"
    }

    this == 0 && zeroIsNa -> "N/A"

    else -> "%,d".format(this)
}

/**
 * Converts an [Int] to a string, returning `"N/A"` if the value is zero.
 *
 * Useful when zero represents a missing or non-applicable value in your domain,
 * and you want to display a more user-friendly fallback.
 *
 * Examples:
 * - `0.toStringOrNa()` -> "N/A"
 * - `123.toStringOrNa()` -> "123"
 *
 * @return The integer as a string, or "N/A" if the value is zero.
 */
fun Int.toStringOrNa(): String = if (this == 0) "N/A" else this.toString()

/**
 * Converts a [Double] to a string, returning `"N/A"` if the value is zero (`0.0`).
 *
 * Useful for displaying numeric values where `0.0` indicates missing or
 * non-applicable data and should be replaced with a more user-friendly string.
 *
 * Examples:
 * - `0.0.toStringOrNa()` -> "N/A"
 * - `123.45.toStringOrNa()` -> "123.45"
 *
 * @return The double as a string, or "N/A" if the value is 0.0.
 */
fun Double.toStringOrNa(): String = if (this == 0.0) "N/A" else this.toString()
