package dev.fizcode.settings.presentation

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle
import dev.fizcode.designsystem.icon.BrandLogo
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.settings.presentation.component.AboutDialog
import dev.fizcode.settings.presentation.component.ConfirmFilterDialog
import dev.fizcode.settings.presentation.component.FilterBlock
import dev.fizcode.settings.presentation.component.FilterSegments
import dev.fizcode.settings.presentation.component.SettingsIcon
import dev.fizcode.settings.presentation.component.SettingsItem
import dev.fizcode.settings.presentation.component.SettingsSection
import dev.fizcode.settings.presentation.component.ThemeDialog
import dev.fizcode.settings.presentation.model.PendingFilterChange
import dev.fizcode.settings.presentation.model.description
import dev.fizcode.settings.presentation.model.icon
import dev.fizcode.settings.presentation.model.label
import dev.fizcode.settings.presentation.model.summary
import dev.fizcode.settings.util.Constant
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val settingsViewModel: SettingsViewModel = koinViewModel()
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SettingsScreenContent(
        uiState = uiState,
        version = remember { context.appVersion() },
        onBackClick = onBackClick,
        onThemeChange = settingsViewModel::setThemeMode,
        onTitleStyleChange = settingsViewModel::setTitleStyle,
        onNsfwFilterChange = settingsViewModel::setNsfwFilter,
        onOpenLink = { url -> context.openLink(url) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState,
    version: String,
    onBackClick: () -> Unit,
    onThemeChange: (ThemeMode) -> Unit,
    onTitleStyleChange: (TitleStyle) -> Unit,
    onNsfwFilterChange: (NsfwFilter) -> Unit,
    onOpenLink: (String) -> Unit
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var pendingChange by remember { mutableStateOf<PendingFilterChange?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                title = { Text(text = Constant.SETTINGS) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = CustomIcon.FILL_ARROW_BACK,
                            contentDescription = Constant.BACK_ICON
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsSection(title = Constant.THEME_SECTION) {
                SettingsItem(
                    title = Constant.THEME_TITLE,
                    subtitle = uiState.themeMode.summary,
                    onClick = { showThemeDialog = true },
                    leading = { SettingsIcon(CustomIcon.FILL_PALETTE, null) }
                )
            }

            SettingsSection(title = Constant.FILTER_SECTION) {
                FilterBlock(
                    icon = CustomIcon.FILL_TEXT_FIELDS,
                    title = Constant.TITLING_TITLE,
                    description = uiState.titleStyle.description
                ) {
                    FilterSegments(
                        options = TitleStyle.entries,
                        selected = uiState.titleStyle,
                        label = { it.label },
                        icon = { it.icon },
                        onSelect = { style ->
                            if (style != uiState.titleStyle) {
                                pendingChange = PendingFilterChange.Titling(style)
                            }
                        }
                    )
                }
                FilterBlock(
                    icon = CustomIcon.FILL_NO_ADULT_CONTENT,
                    title = Constant.NSFW_TITLE,
                    description = uiState.nsfwFilter.description,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    FilterSegments(
                        options = NsfwFilter.entries,
                        selected = uiState.nsfwFilter,
                        label = { it.label },
                        onSelect = { filter ->
                            if (filter != uiState.nsfwFilter) {
                                pendingChange = PendingFilterChange.Nsfw(filter)
                            }
                        }
                    )
                }
            }

            SettingsSection(title = Constant.OTHER_SECTION) {
                SettingsItem(
                    title = Constant.MAL_TITLE,
                    subtitle = Constant.MAL_SUBTITLE,
                    onClick = { onOpenLink(Constant.MAL_URL) },
                    leading = { Logo(BrandLogo.MY_ANIME_LIST) },
                    trailing = { OpenIcon() }
                )
                SettingsItem(
                    title = Constant.ANILIST_TITLE,
                    subtitle = Constant.ANILIST_SUBTITLE,
                    onClick = { onOpenLink(Constant.ANILIST_URL) },
                    leading = { Logo(BrandLogo.ANILIST) },
                    trailing = { OpenIcon() }
                )
                SettingsItem(
                    title = Constant.ABOUT_TITLE,
                    subtitle = Constant.ABOUT_SUBTITLE,
                    onClick = { showAboutDialog = true },
                    leading = { SettingsIcon(CustomIcon.OUTL_INFO, null) }
                )
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline,
                    text = "${Constant.APP_NAME} v$version"
                )
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline,
                    text = Constant.copyright()
                )
            }
        }
    }

    if (showThemeDialog) {
        ThemeDialog(
            selected = uiState.themeMode,
            onSelect = { mode ->
                onThemeChange(mode)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    pendingChange?.let { change ->
        ConfirmFilterDialog(
            change = change,
            onConfirm = {
                when (change) {
                    is PendingFilterChange.Titling -> onTitleStyleChange(change.style)
                    is PendingFilterChange.Nsfw -> onNsfwFilterChange(change.filter)
                }
                pendingChange = null
            },
            onDismiss = { pendingChange = null }
        )
    }

    if (showAboutDialog) {
        AboutDialog(version = version, onDismiss = { showAboutDialog = false })
    }
}

@Composable
private fun Logo(@DrawableRes drawableRes: Int) = Image(
    modifier = Modifier
        .size(24.dp)
        .clip(RoundedCornerShape(4.dp)),
    painter = painterResource(id = drawableRes),
    contentDescription = null
)

@Composable
private fun OpenIcon() = Icon(
    modifier = Modifier.size(24.dp),
    imageVector = CustomIcon.FILL_OPEN_IN_NEW,
    tint = MaterialTheme.colorScheme.onBackground,
    contentDescription = Constant.OPEN_ICON
)

private fun Context.appVersion(): String = runCatching {
    packageManager.getPackageInfo(packageName, 0).versionName
}.getOrNull() ?: Constant.UNKNOWN_VERSION

private fun Context.openLink(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

@Composable
private fun SettingsScreenPreviewContent(uiState: SettingsUiState) {
    AniMangaAnimeAndMangaInfoTheme {
        SettingsScreenContent(
            uiState = uiState,
            version = "0.1.0-Alpha",
            onBackClick = {},
            onThemeChange = {},
            onTitleStyleChange = {},
            onNsfwFilterChange = {},
            onOpenLink = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() = SettingsScreenPreviewContent(SettingsUiState())

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenCustomPreview() = SettingsScreenPreviewContent(
    SettingsUiState(
        themeMode = ThemeMode.DARK,
        titleStyle = TitleStyle.JAPANESE,
        nsfwFilter = NsfwFilter.GREY
    )
)
