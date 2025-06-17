package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetailinfo.model.AnimeCastUiModel
import dev.fizcode.mediadetailinfo.util.Constant

@Composable
internal fun CharacterCast(
    characterData: AnimeCastUiModel,
    onCastImageClick: () -> Unit
) = Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    val shimmer = shimmerBrush()
    AsyncImage(
        modifier = Modifier
            .clickable { onCastImageClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(shimmer)
            .width(50.dp)
            .height(72.dp),
        contentScale = ContentScale.Crop,
        contentDescription = Constant.CHARACTER_IMAGE,
        model = characterData.characterImage
    )
    Column(
        modifier = Modifier.weight(1F)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            text = characterData.character
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Start,
            text = characterData.characterRole
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.End,
            text = characterData.characterRole
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.End,
            text = characterData.voiceActorName
        )
    }
    AsyncImage(
        modifier = Modifier
            .clickable { onCastImageClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(shimmer)
            .width(50.dp)
            .height(72.dp),
        contentScale = ContentScale.Crop,
        contentDescription = Constant.VA_IMAGE,
        model = characterData.voiceActorImage
    )
}

@Composable
internal fun CastShimmer() {
    val shimmer = shimmerBrush()
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(shimmer)
                .width(50.dp)
                .height(72.dp)
        )
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(136.dp)
                    .height(14.dp)
                    .background(shimmer),
            )
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(14.dp)
                    .background(shimmer),
            )
            Box(
                modifier = Modifier
                    .padding(start = 130.dp)
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(shimmer)
            )
            Box(
                modifier = Modifier
                    .padding(start = 170.dp)
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(shimmer)
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(shimmer)
                .width(50.dp)
                .height(72.dp)
        )
    }
}
