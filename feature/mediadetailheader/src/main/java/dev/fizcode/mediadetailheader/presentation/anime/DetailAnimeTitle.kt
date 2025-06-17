package dev.fizcode.mediadetailheader.presentation.anime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetailheader.util.Constant

@Composable
internal fun DetailAnimeTitle(
    posterPath: String,
    title: String,
    mediaType: String,
    releaseSeason: String,
    studios: String
) = Row(
    modifier = Modifier.padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
) {
    AsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(shimmerBrush())
            .width(124.dp)
            .height(178.dp),
        contentScale = ContentScale.Crop,
        contentDescription = Constant.POSTER_IMAGE,
        model = posterPath
    )
    Column {
        Text(
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            text = title
        )
        Spacer(Modifier.height(4.dp))
        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            text = "$mediaType | $releaseSeason"
        )
        Text(
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            text = studios
        )
    }
}
