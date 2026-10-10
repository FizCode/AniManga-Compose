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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import dev.fizcode.dashboard.model.DashboardDestinationItems

/**
 * Bottom bar of the dashboard.
 *
 * @param selectedRoute the currently selected top-level route.
 * @param onItemClick called with the route of the tapped item.
 */
@Composable
fun NavigationBarComponent(
    selectedRoute: NavKey,
    onItemClick: (NavKey) -> Unit
) {

    val screen = listOf(
        DashboardDestinationItems.Anime,
        DashboardDestinationItems.Seasonal,
        DashboardDestinationItems.Manga,
        DashboardDestinationItems.Bookmark
    )

    NavigationBar(
        modifier = Modifier.graphicsLayer {
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            clip = true
        }
    ) {
        screen.forEach { item ->

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
                selected = item.route == selectedRoute,
                onClick = { onItemClick(item.route) }
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
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = MaterialTheme.colorScheme.primary,
        )
    )
}
