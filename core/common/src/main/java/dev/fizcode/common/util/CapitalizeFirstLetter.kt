package dev.fizcode.common.util

import dev.fizcode.common.util.Constant.SPACE

/**
 * Replace first letter to be capitalized.
 * Also it can replace first letter of each word to be capitalized.
 *
 * @param phrase should be String letter or word
 */
fun capitalizeFirstChar(phrase: String): String =
    phrase.split(SPACE).joinToString(SPACE) { it.replaceFirstChar { char -> char.titlecase() } }

