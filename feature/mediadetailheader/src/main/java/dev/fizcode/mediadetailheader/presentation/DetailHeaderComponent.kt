package dev.fizcode.mediadetailheader.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.component.chip.GenreChip
import dev.fizcode.designsystem.component.other.FiveStarReview
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.util.base.shimmerBrush

@Composable
fun DetailHeaderComponent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SliderLargeImage()
        Spacer(modifier = Modifier.height(8.dp))
        DetailAnimeTitle()
        DetailEpisodesInfoCard()
        LittleInfoCard()
        ScoresAndBookmark()
        GenreChips()
    }
}

@Composable
private fun SliderLargeImage() = AsyncImage(
    modifier = Modifier
        .background(shimmerBrush(targetValue = 1300f))
        .fillMaxWidth()
        .height(202.dp),
    contentScale = ContentScale.Crop,
    contentDescription = "",
    model = ""
)

@Composable
private fun DetailAnimeTitle() = Row(
    modifier = Modifier.padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
) {
    AsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(shimmerBrush(targetValue = 1300f))
            .width(124.dp)
            .height(178.dp),
        contentScale = ContentScale.Crop,
        contentDescription = "",
        model = ""
    )
    Column {
        Text(
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            text = "Maou Gakuin no Futekigousha Futekigousha II: Shijou Saikyou no Maou no Shiso, Tensei shite Shison-tachi no Gakkou e Kayou Part 2 from MyAnimeList.net"
        )
        Spacer(Modifier.height(4.dp))
        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            text = "TV | Spring 2024"
        )
        Text(
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            text = "Toei Animation"
        )
    }
}

@Composable
private fun DetailEpisodesInfoCard() = Column(
    modifier = Modifier
        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        .background(MaterialTheme.colorScheme.surfaceContainerLow),
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        text = "Episode 10 of 12 in 6d 2h"
    )
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        text = "23 min. per ep."
    )
}

@Composable
private fun LittleInfoCard() = LazyRow(
    modifier = Modifier
        .padding(vertical = 6.dp),
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(count = 5) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .widthIn(90.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = CustomIcon.OUTL_WORKSPACE_PREMIUM,
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = ""
            )
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            ) {
                Text(
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.labelSmall,
                    text = "Rank"
                )
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    text = "#2"
                )
            }
        }
    }
}

@Composable
private fun ScoresAndBookmark() = Row(
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
            style = MaterialTheme.typography.headlineMedium,
            text = "8.48"
        )
        Column(modifier = Modifier.weight(1F)) {
            FiveStarReview()
            Text("324 Votes")
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
            contentDescription = ""
        )
        Spacer(Modifier.width(8.dp))
        Text("Bookmark")
    }
}

@Composable
private fun GenreChips() = Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
) {
    LazyRow(
        modifier = Modifier.wrapContentWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(count = 9) {
            GenreChip("Lorem")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailHeaderComponentPreview() {
    DetailHeaderComponent()
}