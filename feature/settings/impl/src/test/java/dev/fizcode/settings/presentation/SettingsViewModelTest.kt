package dev.fizcode.settings.presentation

import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle
import dev.fizcode.settings.MainDispatcherRule
import dev.fizcode.settings.awaitValue
import dev.fizcode.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeSettingsRepository : SettingsRepository {
        override val themeMode = MutableStateFlow(ThemeMode.DEFAULT)
        override val titleStyle = MutableStateFlow(TitleStyle.DEFAULT)
        override val nsfwFilter = MutableStateFlow(NsfwFilter.SAFE)

        override suspend fun setThemeMode(value: ThemeMode) { themeMode.value = value }
        override suspend fun setTitleStyle(value: TitleStyle) { titleStyle.value = value }
        override suspend fun setNsfwFilter(value: NsfwFilter) { nsfwFilter.value = value }
    }

    private val repository = FakeSettingsRepository()
    private val viewModel = SettingsViewModel(repository)

    @Test
    fun `starts with the default settings`() = runBlocking {
        val job = viewModel.uiState.onEach { }.launchIn(this)

        assertEquals(SettingsUiState(), viewModel.uiState.awaitValue { true })
        job.cancel()
    }

    @Test
    fun `changes are saved and shown`() = runBlocking {
        val job = viewModel.uiState.onEach { }.launchIn(this)

        viewModel.setThemeMode(ThemeMode.DARK)
        viewModel.setTitleStyle(TitleStyle.ENGLISH)
        viewModel.setNsfwFilter(NsfwFilter.NOT_SAFE)

        val state = viewModel.uiState.awaitValue {
            it.themeMode == ThemeMode.DARK &&
                it.titleStyle == TitleStyle.ENGLISH &&
                it.nsfwFilter == NsfwFilter.NOT_SAFE
        }
        assertEquals(ThemeMode.DARK, repository.themeMode.value)
        assertEquals(TitleStyle.ENGLISH, state.titleStyle)
        job.cancel()
    }
}
