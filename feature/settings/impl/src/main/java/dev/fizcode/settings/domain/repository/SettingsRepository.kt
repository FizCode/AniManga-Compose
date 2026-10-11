package dev.fizcode.settings.domain.repository

import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle
import kotlinx.coroutines.flow.Flow

internal interface SettingsRepository {

    val themeMode: Flow<ThemeMode>
    val titleStyle: Flow<TitleStyle>
    val nsfwFilter: Flow<NsfwFilter>

    suspend fun setThemeMode(value: ThemeMode)

    suspend fun setTitleStyle(value: TitleStyle)

    suspend fun setNsfwFilter(value: NsfwFilter)
}
