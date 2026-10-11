package dev.fizcode.datastore.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal object Constant {
    const val ANIMANGA_DATASTORE = "AniManga Datastore"

    val ONBOARDING = booleanPreferencesKey("ONBOARDING")
    val THEME_MODE = stringPreferencesKey("THEME_MODE")
    val TITLE_STYLE = stringPreferencesKey("TITLE_STYLE")
    val NSFW_FILTER = stringPreferencesKey("NSFW_FILTER")
}
