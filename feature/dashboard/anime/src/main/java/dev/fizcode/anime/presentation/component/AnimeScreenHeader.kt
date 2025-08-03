package dev.fizcode.anime.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.anime.util.Constant
import dev.fizcode.designsystem.component.other.SearchSimpleRowComponent
import dev.fizcode.designsystem.icon.CustomIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnimeScreenHeader(
    scrollBehavior: TopAppBarScrollBehavior,
    value: String,
    onClickSettings: () -> Unit,
    onClickSearch: () -> Unit
) = TopAppBar(
    scrollBehavior = scrollBehavior,
    title = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchSimpleRowComponent(
                modifier = Modifier.weight(1F),
                value = value,
                onClickSearch = onClickSearch
            )
            IconButton(onClick = onClickSettings) {
                Icon(
                    imageVector = CustomIcon.ROUND_SETTINGS,
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = Constant.SETTINGS_ICON
                )
            }
            Spacer(Modifier.width(4.dp))
        }
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun AnimeScreenHeaderPreview() {
    AnimeScreenHeader(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        value = "Search",
        onClickSettings = {},
        onClickSearch = {}
    )
}
