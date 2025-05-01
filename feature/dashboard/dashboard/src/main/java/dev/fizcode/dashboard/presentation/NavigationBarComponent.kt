package dev.fizcode.dashboard.presentation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.fizcode.dashboard.model.DashboardDestinationItems
import dev.fizcode.anime.navigation.AnimeRoute

@Composable
fun NavigationBarComponent(navHostController: NavHostController) {

    val screen = listOf(
        DashboardDestinationItems.Anime,
        DashboardDestinationItems.Seasonal,
        DashboardDestinationItems.Manga,
        DashboardDestinationItems.Bookmark
    )

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
        ?: AnimeRoute::class.qualifiedName.orEmpty() // TODO: Change default to EmptyScreen

    NavigationBar(
        modifier = Modifier.graphicsLayer {
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            clip = true
        }
    ) {
        screen.forEach { item ->

            val isSelected by remember(currentDestination) {
                derivedStateOf { currentDestination == item.route::class.qualifiedName }
            }

            AddItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                selectedIcon = {
                    Icon(imageVector = item.selectedIcon, contentDescription = item.title)
                },
                label = {
                    Text(text = item.title)
                },
                selected = isSelected,
                onClick = {
                    navHostController.navigate(item.route) {
                        popUpTo(navHostController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.AddItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    onClick: () -> Unit
) {
    NavigationBarItem(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
        )
    )
}
