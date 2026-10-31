package dev.fizcode.mediadetails.util

internal fun List<String>.toStringWithComma(): String = this.joinToString(", ")

internal fun <T> List<T>.toStringWithComma(selector: (T) -> String): String =
    this.joinToString(", ") { selector(it) }
