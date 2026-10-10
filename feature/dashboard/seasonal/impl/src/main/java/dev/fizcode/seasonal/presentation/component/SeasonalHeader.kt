package dev.fizcode.seasonal.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.other.SearchTextFieldComponent
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.seasonal.util.Constant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Search field on the left, the selected [year] and a filter button on the right.
 * Tapping either of the latter opens a menu to pick another year from [years].
 */
@Composable
internal fun SeasonalHeader(
    queryState: TextFieldState,
    year: Int,
    years: ImmutableList<Int>,
    onYearSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchTextFieldComponent(
            modifier = Modifier.weight(1f),
            state = queryState,
            placeholder = Constant.PLACEHOLDER
        )
        Box {
            Row(
                modifier = Modifier.clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    text = year.toString()
                )
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        imageVector = CustomIcon.ROUND_FILTER_LIST,
                        contentDescription = Constant.FILTER_ICON
                    )
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                years.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.toString()) },
                        onClick = {
                            expanded = false
                            onYearSelect(item)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SeasonalHeaderPreview() {
    SeasonalHeader(
        queryState = rememberTextFieldState(),
        year = 2026,
        years = persistentListOf(2027, 2026, 2025),
        onYearSelect = {}
    )
}
