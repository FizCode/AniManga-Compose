package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.mediadetailinfo.model.AnimeRelatedUiModel

@Composable
internal fun RelatedAnime(
    index: Int,
    relatedData: AnimeRelatedUiModel,
    onClickRelated: (Int) -> Unit,
) = Row(
    modifier = Modifier
        .clickable { onClickRelated(relatedData.relatedId) }
        .padding(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    Text(
        modifier = Modifier.align(Alignment.CenterVertically),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Start,
        text = index.toString()
    )
    Column(
        modifier = Modifier.weight(1F)
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
    val dummyData = AnimeRelatedUiModel(
        relatedId = 0,
        name = "Dummy Season 2",
        relationType = "Manga",
        url = ""
    )
    RelatedAnime(
        index = 1,
        relatedData = dummyData,
        onClickRelated = {}
    )
}

