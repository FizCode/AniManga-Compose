package dev.fizcode.common.util

import dev.fizcode.common.util.Constant.SPACE

/**
 * Capitalizes the first letter of each word in a given phrase.
 *
 * This function splits the input `phrase` by spaces, and then capitalizes
 * the first letter of each word while keeping the rest of the word in lowercase.
 * It then joins the words back into a single string with spaces separating them.
 *
 * Useful for formatting phrases, titles, or names that require proper capitalization.
 *
 * @param phrase The input string to be formatted. Each word's first letter will be capitalized.
 * @return A new string where the first letter of each word is capitalized.
 *
 * Example:
 * ```
 * capitalizeFirstChar("sunday") // returns: "Sunday"
 * capitalizeFirstChar("TO BE ANNOUNCED") // returns: "To Be Announced"
 * capitalizeFirstChar("sTARTING SOON") // returns: "Starting Soon"
 * ```
 */
fun capitalizeFirstChar(phrase: String): String =
    phrase.split(SPACE).joinToString(SPACE) { it.replaceFirstChar { char -> char.titlecase() } }
