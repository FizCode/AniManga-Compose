package dev.fizcode.animanga.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.fizcode.animanga.navigation.AppNavDisplay
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.datastore.AniMangaDatastore
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.mediadetails.navigation.toMediaDetailsRouteOrNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import androidx.compose.runtime.CompositionLocalProvider
import dev.fizcode.common.settings.LocalTitleStyle
import dev.fizcode.common.settings.TitleStyle

class MainActivity : ComponentActivity() {

    private val datastore: AniMangaDatastore by inject()

    private var deepLinkRoute by mutableStateOf<MediaDetailsRoute?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // A recreated activity restores its back stack, so only a fresh launch handles the link.
        if (savedInstanceState == null) {
            deepLinkRoute = intent.data?.toMediaDetailsRouteOrNull()
        }
        // Read once up front so a dark theme does not start with a light frame.
        val initialThemeMode = runBlocking { datastore.getThemeMode().first() }
        setContent {
            val themeMode by remember { datastore.getThemeMode() }
                .collectAsState(initial = initialThemeMode)
            val darkTheme = when (themeMode) {
                ThemeMode.DEFAULT -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            val titleStyle by remember { datastore.getTitleStyle() }
                .collectAsState(initial = TitleStyle.DEFAULT)

            // The system bar icons follow the app theme, not the system one.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme }
                )
                onDispose { }
            }

            AniMangaAnimeAndMangaInfoTheme(darkTheme = darkTheme) {
                CompositionLocalProvider(LocalTitleStyle provides titleStyle) {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        AppNavDisplay(
                            deepLinkRoute = deepLinkRoute,
                            onDeepLinkHandled = { deepLinkRoute = null }
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        deepLinkRoute = intent.data?.toMediaDetailsRouteOrNull()
    }
}
