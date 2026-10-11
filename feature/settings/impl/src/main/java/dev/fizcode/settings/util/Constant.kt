package dev.fizcode.settings.util

import java.util.Calendar

internal object Constant {

    const val ABOUT_DESCRIPTION =
        "AniManga Info is a free app to browse anime and manga, built by FizCode to study Android " +
            "development. All data comes from MyAnimeList and Anilist."
    const val ABOUT_TITLE = "About"
    const val ABOUT_SUBTITLE = "About this App"
    const val ANILIST_SUBTITLE = "Go to Anilist API"
    const val ANILIST_TITLE = "Anilist"
    const val ANILIST_URL = "https://docs.anilist.co"
    const val APP_NAME = "AniManga Info"
    const val BACK_ICON = "Back"
    const val CANCEL = "Cancel"
    const val CHANGE = "Change"
    const val CLOSE = "Close"
    const val FILTER_SECTION = "Filter"
    const val MAL_SUBTITLE = "Go to MAL web page"
    const val MAL_TITLE = "MyAnimeList"
    const val MAL_URL = "https://myanimelist.net"
    const val NSFW_DIALOG_TITLE = "Change NSFW filter?"
    const val NSFW_TITLE = "NSFW"
    const val OPEN_ICON = "Open"
    const val OTHER_SECTION = "Other"
    const val SETTINGS = "Settings"
    const val STOP_TIMEOUT_MILLIS = 5_000L
    const val THEME_SECTION = "Theme"
    const val THEME_TITLE = "Theme"
    const val TITLING_DIALOG_TITLE = "Change titling style?"
    const val TITLING_TITLE = "Titling Style"
    const val UNKNOWN_VERSION = "unknown"

    /** Always shows the current [year], so it does not need an update every new year. */
    fun copyright(year: Int = Calendar.getInstance().get(Calendar.YEAR)) =
        "Copyright© $year FizCode. All Rights Reserved."

    fun confirmChange(label: String) = "Are you sure you want to change to $label?"
}
