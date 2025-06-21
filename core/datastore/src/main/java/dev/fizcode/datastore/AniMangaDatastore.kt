package dev.fizcode.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dev.fizcode.datastore.util.Constant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AniMangaDatastore(private val context: Context) {

    companion object {
        val Context.aniMangaDatastore: DataStore<Preferences> by preferencesDataStore(
            name = Constant.ANIMANGA_DATASTORE,
            produceMigrations = Companion::sharedPreferencesMigration
        )

        private fun sharedPreferencesMigration(context: Context) =
            listOf(SharedPreferencesMigration(context, Constant.ANIMANGA_DATASTORE))
    }

    suspend fun setOnboarding(value: Boolean) = context.aniMangaDatastore.edit { preferences ->
        preferences[Constant.ONBOARDING] = value
    }

    fun getOnboarding(): Flow<Boolean> = context.aniMangaDatastore.data.map { preferences ->
        preferences[Constant.ONBOARDING] ?: false
    }
}
