package dev.fizcode.common.util.extensions

/**
 * Returns the original string if it is not null or blank,
 * otherwise returns a dash ("-").
 *
 * This is useful for displaying placeholder text when a string value
 * from an API or user input is null or empty.
 *
 * @receiver The string to evaluate. Can be null.
 * @return The original string if not null or blank, otherwise a dash ("-").
 */
fun String?.orDash() = if (this.isNullOrBlank()) "-" else this

/**
 * Returns the current integer value if it's not null,
 * otherwise returns 0.
 *
 * This is useful to avoid null checks when displaying or using optional integers.
 *
 * @return the integer value or 0 if the value is null.
 */
fun Int?.orZero() = this ?: 0

/**
 * Returns the current double value if it's not null,
 * otherwise returns 0.0.
 *
 * This is useful to avoid null checks when dealing with optional double values.
 *
 * @return the double value or 0.0 if the value is null.
 */
fun Double?.orZero() = this ?: 0.0

/**
 * Returns a single list of dash ("-") if the list is null or empty,
 * otherwise returns the string representation of the list.
 *
 * This is useful for displaying fallback text when dealing with optional or empty lists.
 *
 * @return a list of single string dash ("-") if the list is null or empty.
 */
fun List<String>?.orDashList(): List<String> = if (this.isNullOrEmpty()) listOf("-") else this
