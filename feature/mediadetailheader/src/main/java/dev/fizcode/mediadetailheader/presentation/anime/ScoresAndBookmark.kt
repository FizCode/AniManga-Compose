package dev.fizcode.mediadetailheader.presentation.anime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.other.FiveStarReview
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.mediadetailheader.util.Constant

@Composable
internal fun ScoresAndBookmark(
    score: String,
    stars: Double,
    totalVote: String
) = Row(
    modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
) {
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
        onClick = {}
    ) {
        Icon(
            imageVector = CustomIcon.OUTL_BOOKMARK_ADD,
            contentDescription = Constant.BOOKMARK_ICON
        )
        Spacer(Modifier.width(8.dp))
        Text(Constant.BOOKMARK)
    }
}
