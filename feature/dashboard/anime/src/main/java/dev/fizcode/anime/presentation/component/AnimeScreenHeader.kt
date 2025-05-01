package dev.fizcode.anime.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.anime.util.Constant.AnimeSearchHeader
import dev.fizcode.designsystem.icon.MaterialIcon

@Composable
internal fun AnimeScreenHeader(
    value: String,
    onValueChanged: (String) -> Unit,
    onClickSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(100.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                ),
            leadingIcon = {
                Icon(
                    imageVector = MaterialIcon.FILLED_SEARCH,
                    contentDescription = AnimeSearchHeader.SEARCH_ICON
                )
            },
            value = value,
            onValueChange = onValueChanged
        )
        IconButton(onClick = onClickSettings) {
            Icon(
                imageVector = MaterialIcon.OUTLINED_SETTINGS,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = AnimeSearchHeader.SETTINGS_ICON
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimeScreenHeaderPreview() {
    AnimeScreenHeader(
        value = "Search",
        onValueChanged = { _ -> },
        onClickSettings = {}
    )
}
