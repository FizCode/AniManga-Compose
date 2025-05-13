package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.other.HyperlinkText
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.mediadetailinfo.model.InformationTableUiModel

@Composable
internal fun InformationTabComponent() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val infoTableDummy = listOf(
            InformationTableUiModel(
                title = "Type",
                desc = "TV",
                type = "text"
            ),
            InformationTableUiModel(
                title = "Episodes",
                desc = "24",
                type = "text"
            ),
            InformationTableUiModel(
                title = "Status",
                desc = "Finished Airing",
                type = "text"
            ),
            InformationTableUiModel(
                title = "Aired",
                desc = "Spring 2021",
                type = "text"
            ),
            InformationTableUiModel(
                title = "Rating",
                desc = "PG-13",
                type = "text"
            ),
            InformationTableUiModel(
                title = "Twitter",
                desc = "@anime_official",
                type = "sns",
                link = "https://twitter.com/anime_official"
            ),
            InformationTableUiModel(
                title = "Website",
                desc = "Visit site",
                type = "link",
                link = "https://example.com"
            )
        )


        InfoTable(
            title = "Information",
            tableContent = infoTableDummy
        )
        InfoTable(
            title = "Statistic",
            tableContent = infoTableDummy
        )
        LinkTable(
            title = "Available At",
            tableContent = infoTableDummy
        )
    }
}

@Composable
private fun InfoTable(
    title: String,
    tableContent: List<InformationTableUiModel>
) {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            text = title
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            tableContent.forEach { data ->
                TextTable(title = data.title, desc = data.desc, data.link)
            }
        }
    }
}

@Composable
private fun LinkTable(
    title: String,
    tableContent: List<InformationTableUiModel>
) {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            text = title
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            tableContent.forEach { data ->
                IconLinkTable(icon = data.snsIcon, text = data.desc, link = data.link)
            }
        }
    }
}

@Composable
private fun TextTable(
    title: String,
    desc: String,
    link: String
) {
    Row {
        Text(
            modifier = Modifier.width(100.dp),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            text = title
        )

        if (link.isNotEmpty()) {
            HyperlinkText(
                modifier = Modifier.weight(1F),
                style = MaterialTheme.typography.bodyMedium,
                text = desc,
                url = link
            )
        } else {
            Text(
                modifier = Modifier.weight(1F),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                text = desc
            )
        }
    }
}

@Composable
private fun IconLinkTable(
    icon: ImageVector = CustomIcon.FILL_LINK,
    text: String,
    link: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary,
            imageVector = icon,
            contentDescription = "SNS ICON"
        )
        HyperlinkText(
            modifier = Modifier.weight(1F),
            style = MaterialTheme.typography.bodyMedium,
            text = text,
            url = link
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InformationTabPreview() {
    InformationTabComponent()
}

