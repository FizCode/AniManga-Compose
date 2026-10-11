package dev.fizcode.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle
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

    fun getThemeMode(): Flow<ThemeMode> = enumFlow(Constant.THEME_MODE, ThemeMode.DEFAULT)

    suspend fun setThemeMode(value: ThemeMode) = setEnum(Constant.THEME_MODE, value)

    fun getTitleStyle(): Flow<TitleStyle> = enumFlow(Constant.TITLE_STYLE, TitleStyle.DEFAULT)

    suspend fun setTitleStyle(value: TitleStyle) = setEnum(Constant.TITLE_STYLE, value)

    fun getNsfwFilter(): Flow<NsfwFilter> = enumFlow(Constant.NSFW_FILTER, NsfwFilter.SAFE)

    suspend fun setNsfwFilter(value: NsfwFilter) = setEnum(Constant.NSFW_FILTER, value)

    /** Stored by name; a missing or unknown name (e.g. after a rename) reads as [default]. */
    private inline fun <reified T : Enum<T>> enumFlow(
        key: Preferences.Key<String>,
        default: T
    ): Flow<T> = context.aniMangaDatastore.data.map { preferences ->
        enumValues<T>().firstOrNull { it.name == preferences[key] } ?: default
    }

    private suspend fun <T : Enum<T>> setEnum(key: Preferences.Key<String>, value: T) {
        context.aniMangaDatastore.edit { preferences -> preferences[key] = value.name }
    }
}
