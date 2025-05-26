package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.common.util.orDash
import dev.fizcode.designsystem.component.other.HyperlinkText
import dev.fizcode.designsystem.component.other.MultipleHyperlinkText
import dev.fizcode.mediadetailinfo.model.AnimeInfo
import dev.fizcode.mediadetailinfo.model.TextTableData
import dev.fizcode.mediadetailinfo.util.Constant

@Composable
internal fun InformationTabComponent(
    animeInfo: AnimeInfo
) {
    val informationList = listOf(
        TextTableData(title = Constant.TYPE, desc = animeInfo.type),
        TextTableData(title = Constant.EPISODES, desc = animeInfo.episodes),
        TextTableData(title = Constant.STATUS, desc = animeInfo.status),
        TextTableData(title = Constant.AIRED, desc = animeInfo.aired),
        TextTableData(
            title = Constant.PREMIERED,
            desc = animeInfo.premiered,
            link = listOf(animeInfo.premieredUrl)
        ),
        TextTableData(title = Constant.BROADCAST, desc = animeInfo.aired),
        TextTableData(
            title = Constant.PRODUCERS,
            listDesc = animeInfo.producers.map { it.name },
            link = animeInfo.producers.map { it.link }),
        TextTableData(
            title = Constant.LICENSORS,
            listDesc = animeInfo.licensors.map { it.name },
            link = animeInfo.licensors.map { it.link }),
        TextTableData(
            title = Constant.STUDIOS,
            listDesc = animeInfo.studios.map { it.name },
            link = animeInfo.studios.map { it.link }),
        TextTableData(
            title = Constant.SOURCE,
            desc = animeInfo.source.name,
            link = listOf(animeInfo.source.link)
        ),
        TextTableData(
            title = Constant.GENRES,
            listDesc = animeInfo.genre.map { it.name },
            link = animeInfo.genre.map { it.link }),
        TextTableData(
            title = Constant.THEMES,
            listDesc = animeInfo.themes.map { it.name },
            link = animeInfo.themes.map { it.link }),
        TextTableData(title = Constant.DURATION, desc = animeInfo.duration),
        TextTableData(title = Constant.RATING, desc = animeInfo.rating),
    )

    val statisticsList = listOf(
        TextTableData(title = Constant.SCORE, desc = animeInfo.score),
        TextTableData(title = Constant.RANKED, desc = animeInfo.ranked),
        TextTableData(title = Constant.POPULARITY, desc = animeInfo.popularity),
        TextTableData(title = Constant.MEMBERS, desc = animeInfo.members),
        TextTableData(title = Constant.FAVORITES, desc = animeInfo.favorites),
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoTable(Constant.INFORMATION, informationList)
        InfoTable(Constant.STATISTICS, statisticsList)
    }
}

@Composable
private fun InfoTable(
    title: String,
    items: List<TextTableData>
) {
    Column(
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
}

@Composable
private fun TextTable(
    modifier: Modifier,
    title: String,
    desc: String = "",
    listDesc: List<String> = emptyList(),
    link: List<String> = emptyList()
) {
    Row {
        Text(
            modifier = Modifier.width(100.dp),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            text = title
        )

        when {

            desc.isNotEmpty() && listDesc.isEmpty() -> {
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
                    fullText = listDesc.joinToString(", "),
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InformationTabPreview() {
    InformationTabComponent(animeInfo = AnimeInfo())
}
