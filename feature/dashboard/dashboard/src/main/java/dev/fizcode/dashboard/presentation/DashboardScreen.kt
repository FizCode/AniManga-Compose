package dev.fizcode.dashboard.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.fizcode.dashboard.navigation.NavigationBarNavGraph

@Composable
fun DashboardScreen(
    navHostController: NavHostController = rememberNavController(),
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    Scaffold(
        bottomBar = { NavigationBarComponent(navHostController = navHostController) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavigationBarNavGraph(
            navHostController = navHostController,
            innerPadding = innerPadding,
            onCardClick = onCardClick
        )
    }
}
