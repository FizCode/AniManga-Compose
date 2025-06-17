package dev.fizcode.common.util.extensions

import dev.fizcode.common.util.Constant.SPACE

/**
 * Capitalizes the first character of each word in the string.
 *
 * This function splits the original string by spaces and applies [String.replaceFirstChar]
 * with [Char.titlecase] to each word, then joins them back into a single string.
 *
 * Example:
 * ```
 * "sunday, 1".toCapitalFirstChar() // returns "Sunday, 1"
 * "TO BE ANNOUNCED".toCapitalFirstChar() // returns "To Be Announced"
 * "sTARTING SoON".toCapitalFirstChar() // returns "Starting Soon"
 * ```
 *
 * @return A new string where each word starts with an uppercase letter.
 */
fun String.toCapitalFirstChar(): String =
    this.split(SPACE).joinToString(SPACE) { it.replaceFirstChar { char -> char.titlecase() } }
