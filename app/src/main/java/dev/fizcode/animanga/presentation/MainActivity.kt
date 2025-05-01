package dev.fizcode.animanga.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.fizcode.animanga.model.InitialDestinationRoute
import dev.fizcode.animanga.navigation.RootNavGraph
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AniMangaAnimeAndMangaInfoTheme {
               Surface(
                   color = MaterialTheme.colorScheme.background,
               ) {
                   RootNavGraph(
                       navHostController = rememberNavController(),
                       startDestination = InitialDestinationRoute.Dashboard.route
                   )
               }
            }
        }
    }
}
