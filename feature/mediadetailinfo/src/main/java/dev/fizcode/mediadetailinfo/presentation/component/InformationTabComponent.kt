package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.common.util.extensions.orDash
import dev.fizcode.common.util.extensions.toStringWithComma
import dev.fizcode.designsystem.component.other.HyperlinkText
import dev.fizcode.designsystem.component.other.MultipleHyperlinkText
import dev.fizcode.mediadetailinfo.model.AnimeInfo
import dev.fizcode.mediadetailinfo.model.AnimeRelatedUiModel
import dev.fizcode.mediadetailinfo.model.TextTableData
import dev.fizcode.mediadetailinfo.presentation.util.buildInformationList
import dev.fizcode.mediadetailinfo.presentation.util.buildStatisticsList
import dev.fizcode.mediadetailinfo.util.Constant
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun InformationTabComponent(
    animeInfo: AnimeInfo
) {
    val informationList = remember(animeInfo) { buildInformationList(animeInfo) }
    val statisticsList = remember(animeInfo) { buildStatisticsList(animeInfo) }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoTable(Constant.INFORMATION, informationList)
        InfoTable(Constant.STATISTICS, statisticsList)
        RelatedAnimeList(
            relatedAnime = animeInfo.relatedAnime,
            onClickRelated = { _ -> }
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun InfoTable(
    title: String,
    items: List<TextTableData>
) = Column(
    modifier = Modifier.padding(top = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textAlign = TextAlign.Center,
        text = title
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        items.forEach {
            TextTable(
                modifier = Modifier.weight(1F),
                title = it.title,
                desc = it.desc,
                listDesc = it.listDesc,
                link = it.link
            )
        }
    }
}

@Composable
private fun TextTable(
    modifier: Modifier,
    title: String,
    desc: String = "",
    listDesc: List<String> = emptyList(),
    link: List<String> = emptyList()
) = Row {
    Text(
        modifier = Modifier.width(100.dp),
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        text = title
    )

    when {

        desc.isNotBlank() && listDesc.isNotEmpty() -> {
            HyperlinkText(
                modifier = modifier,
                style = MaterialTheme.typography.bodyMedium,
                text = desc,
                url = link.firstOrNull().orEmpty()
            )
        }

        listDesc.isNotEmpty() && link.size == listDesc.size -> {
            MultipleHyperlinkText(
                modifier = modifier,
                style = MaterialTheme.typography.bodyMedium,
                fullText = listDesc.toStringWithComma(),
                linkText = listDesc,
                hyperlinks = link
            )
        }

        else -> {
            Text(
                modifier = modifier,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                text = desc.orDash()
            )
        }
    }

}

@Composable
private fun RelatedAnimeList(
    relatedAnime: ImmutableList<AnimeRelatedUiModel>,
    onClickRelated: (Int) -> Unit
) = Column(
    modifier = Modifier.padding(top = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textAlign = TextAlign.Center,
        text = "Related Anime"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        relatedAnime.forEach { data ->
            key(data.relatedId) {
                RelatedAnime(
                    relatedData = data,
                    onClickRelated = onClickRelated
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InformationTabPreview() {
    InformationTabComponent(animeInfo = AnimeInfo())
}
