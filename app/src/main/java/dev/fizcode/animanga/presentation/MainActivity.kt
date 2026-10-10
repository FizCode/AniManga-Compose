package dev.fizcode.animanga.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.fizcode.animanga.navigation.AppNavDisplay
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.api.MediaDetailsRoute
import dev.fizcode.mediadetails.navigation.toMediaDetailsRouteOrNull

class MainActivity : ComponentActivity() {

    private var deepLinkRoute by mutableStateOf<MediaDetailsRoute?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // A recreated activity restores its back stack, so only a fresh launch handles the link.
        if (savedInstanceState == null) {
            deepLinkRoute = intent.data?.toMediaDetailsRouteOrNull()
        }
        setContent {
            AniMangaAnimeAndMangaInfoTheme {
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        deepLinkRoute = intent.data?.toMediaDetailsRouteOrNull()
    }
}
