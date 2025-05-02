package dev.fizcode.dashboard.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.fizcode.dashboard.navigation.NavigationBarNavGraph

@Composable
fun DashboardScreen(
    navHostController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { NavigationBarComponent(navHostController = navHostController) }
    ) { innerPadding ->
        NavigationBarNavGraph(
            navHostController = navHostController,
            innerPadding = innerPadding
        )
    }
}
