package dev.fizcode.settings.data

import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle
import dev.fizcode.datastore.AniMangaDatastore
import dev.fizcode.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

internal class SettingsRepositoryImpl(
    private val datastore: AniMangaDatastore
) : SettingsRepository {

    override val themeMode: Flow<ThemeMode> = datastore.getThemeMode()
    override val titleStyle: Flow<TitleStyle> = datastore.getTitleStyle()
    override val nsfwFilter: Flow<NsfwFilter> = datastore.getNsfwFilter()

    override suspend fun setThemeMode(value: ThemeMode) = datastore.setThemeMode(value)

    override suspend fun setTitleStyle(value: TitleStyle) = datastore.setTitleStyle(value)

    override suspend fun setNsfwFilter(value: NsfwFilter) = datastore.setNsfwFilter(value)
}
