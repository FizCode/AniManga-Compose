package dev.fizcode.datastore.util

import androidx.datastore.preferences.core.booleanPreferencesKey

internal object Constant {
    const val ANIMANGA_DATASTORE = "AniManga Datastore"

    val ONBOARDING = booleanPreferencesKey("ONBOARDING")
}
