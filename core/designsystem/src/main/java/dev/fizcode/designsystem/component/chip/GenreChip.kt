package dev.fizcode.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GenreChip(
    genre: String
) {
    Row(
        Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.labelMedium,
            text = genre
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreChipPreview() {
    GenreChip(genre = "Action")
}
