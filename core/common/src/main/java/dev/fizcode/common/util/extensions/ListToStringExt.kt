package dev.fizcode.common.util.extensions

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
