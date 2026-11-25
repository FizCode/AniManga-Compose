package dev.fizcode.mediadetails.presentation.info.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import dev.fizcode.designsystem.theme.AniMangaTheme
import dev.fizcode.designsystem.util.base.shimmer
import dev.fizcode.mediadetails.presentation.info.model.AnimeRelatedUiModel
import dev.fizcode.mediadetails.presentation.info.util.Constant

@Composable
internal fun RelatedAnime(
    relatedData: AnimeRelatedUiModel,
    onClickRelated: (Int) -> Unit,
) = Row(
    modifier = Modifier
        .height(IntrinsicSize.Min)
        .clickable { onClickRelated(relatedData.relatedId) }
        .padding(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    // Not AsyncImage: its intrinsic size would make the row taller than the text.
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(2f / 3f, matchHeightConstraintsFirst = true)
            .clip(RoundedCornerShape(4.dp))
            .shimmer()
            .paint(
                painter = rememberAsyncImagePainter(model = relatedData.url),
                sizeToIntrinsics = false,
                contentScale = ContentScale.Crop
            )
            .semantics { contentDescription = Constant.RELATED_IMAGE }
    )
    Column(
        modifier = Modifier
            .weight(1F)
            .align(Alignment.CenterVertically)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            text = relatedData.name
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Start,
            text = relatedData.relationType
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RelatedAnimePreview() {
    AniMangaTheme {
        RelatedAnime(
            relatedData = AnimeRelatedUiModel(
                relatedId = 0,
                name = "Dummy Season 2",
                relationType = "Sequel",
                url = ""
            ),
            onClickRelated = {}
        )
    }
}
