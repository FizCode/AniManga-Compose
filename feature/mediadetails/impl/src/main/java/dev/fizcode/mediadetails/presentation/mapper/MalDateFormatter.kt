package dev.fizcode.mediadetails.presentation.mapper

import dev.fizcode.mediadetails.util.Constant

internal object MalDateFormatter {

    private val MONTHS = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    /** Formats MAL's `yyyy-MM-dd` dates (month and day may be missing) as `Oct 5, 2024 to Dec 28, 2024`. */
    fun range(startDate: String, endDate: String): String {
        val start = label(startDate)
        val end = label(endDate)
        return when {
            start.isEmpty() -> Constant.NOT_AVAILABLE
            end.isEmpty() -> "$start to ?"
            start == end -> start
            else -> "$start to $end"
        }
    }

    private fun label(date: String): String {
        val parts = date.split("-")
        val year = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: return ""
        val month = parts.getOrNull(1)?.toIntOrNull()?.let { MONTHS.getOrNull(it - 1) }
            ?: return year
        val day = parts.getOrNull(2)?.toIntOrNull() ?: return "$month $year"
        return "$month $day, $year"
    }
}
