package dev.fizcode.common.media

enum class AiringStatus(val apiValue: String, val label: String) {
    FINISHED("finished_airing", "Finished"),
    CURRENTLY_AIRING("currently_airing", "Currently Airing"),
    NOT_YET_AIRED("not_yet_aired", "Not Yet Aired"),
    UNKNOWN("", "Unknown Airing Status");

    companion object {
        fun fromApi(value: String): AiringStatus = entries.firstOrNull { it.apiValue == value } ?: UNKNOWN
    }
}
