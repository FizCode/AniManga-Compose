package dev.fizcode.search.presentation.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * Colors every case-insensitive occurrence of [query] inside [text] with [color].
 */
internal fun highlightQuery(
    text: String,
    query: String,
    color: Color
): AnnotatedString = buildAnnotatedString {
    val keyword = query.trim()
    var cursor = 0

    while (keyword.isNotEmpty()) {
        val start = text.indexOf(keyword, startIndex = cursor, ignoreCase = true)
        if (start < 0) break

        append(text.substring(cursor, start))
        withStyle(SpanStyle(color = color)) {
            append(text.substring(start, start + keyword.length))
        }
        cursor = start + keyword.length
    }

    append(text.substring(cursor))
}
