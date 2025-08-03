package dev.fizcode.mediadetailheader.presentation.anime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.other.FiveStarReview
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.mediadetailheader.util.Constant

@Composable
internal fun ScoresAndBookmark(
    isBookmarked: Boolean,
    score: String,
    stars: Double,
    totalVote: String,
    onClickBookmark: () -> Unit
) = Row(
    modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
) {
    val buttonColor = if (isBookmarked) {
        ButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    val buttonIcon =
        if (isBookmarked) CustomIcon.OUTL_BOOKMARK_REMOVE else CustomIcon.OUTL_BOOKMARK_ADD
    val buttonText = if (isBookmarked) Constant.REMOVE_BOOKMARK else Constant.BOOKMARK
    Row(
        modifier = Modifier.weight(1F),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1F),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium,
            text = score
        )
        Column(modifier = Modifier.weight(1F)) {
            FiveStarReview(score = stars)
            Text(
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                text = "$totalVote ${Constant.VOTES}"
            )
        }
    }
    Spacer(Modifier.width(8.dp))
    Button(
        modifier = Modifier
            .weight(1F)
            .padding(vertical = 8.dp),
        colors = buttonColor,
        onClick = onClickBookmark
    ) {
        Icon(
            imageVector = buttonIcon,
            contentDescription = Constant.BOOKMARK_ICON
        )
        Spacer(Modifier.width(8.dp))
        Text(buttonText)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScoresAndBookmarkPreview() {
    ScoresAndBookmark(
        isBookmarked = false,
        score = "5.00",
        stars = 5.0,
        totalVote = "1,000",
        onClickBookmark = {}
    )
}
