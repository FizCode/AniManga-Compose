package dev.fizcode.common.util.extensions

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Converts a list of strings into a comma-separated string.
 *
 * This extension function joins all the elements in the list with a comma and a space (", ")
 * to produce a readable string format.
 *
 * Example:
 * ```
 * val genres = listOf("Action", "Comedy", "Fantasy")
 * val result = genres.toStringWithComma()
 * // result: "Action, Comedy, Fantasy"
 * ```
 *
 * @return A comma-separated string of the list elements.
 */
fun List<String>.toStringWithComma(): String = this.joinToString(", ")

/**
 * Converts a list of items into a comma-separated string using a selector function.
 *
 * This extension function maps each element of the list to a string using the provided [selector],
 * then joins them with a comma and a space (", ") to form a readable string.
 *
 * Example:
 * ```
 * data class Studio(val name: String)
 * val studios = listOf(Studio("A-1 Pictures"), Studio("Bones"))
 * val result = studios.toStringWithComma { it.name }
 * // result: "A-1 Pictures, Bones"
 * ```
 *
 * @param selector A lambda function that maps each item in the list to a string.
 * @return A comma-separated string of the mapped values.
 */
fun <T> List<T>.toStringWithComma(selector: (T) -> String): String =
    this.joinToString(", ") { selector(it) }

/**
 * Converts a comma-separated string into a list of strings.
 *
 * This extension function splits the string using a comma (",") as the delimiter,
 * optionally trimming whitespace from each item.
 *
 * Example:
 * ```
 * val genreString = "Action, Comedy, Fantasy"
 * val result = genreString.toList()
 * // result: listOf("Action", "Comedy", "Fantasy")
 * ```
 *
 * @return A list of trimmed strings split from the original string using "," as the separator.
 */
fun String.toList(): List<String> = this.split(",").map { it.trim() }

/**
 * Converts a comma-separated string into an [ImmutableList] of strings.
 *
 * This extension function splits the string using a comma (",") as the delimiter,
 * trimming any surrounding whitespace from each item, and then converts the result
 * into an immutable list using Kotlinx Collections Immutable.
 *
 * Example:
 * ```
 * val genreString = "Action, Comedy, Fantasy"
 * val result = genreString.toImmutableList()
 * // result: persistentListOf("Action", "Comedy", "Fantasy")
 * ```
 *
 * @return An [ImmutableList] of trimmed strings split from the original string using "," as the separator.
 */
fun String.toImmutableList(): ImmutableList<String> =
    this.split(",").map { it.trim() }.toImmutableList()
