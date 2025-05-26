package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.mediadetailinfo.model.AnimeDetails
import dev.fizcode.mediadetailinfo.util.Constant

@Composable
internal fun DetailsTabComponent(
    animeDetails: AnimeDetails,
    onClickShare: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExpandableTitles(
            synonym = animeDetails.synonym,
            japanese = animeDetails.japanese,
            english = animeDetails.english
        )
        if (animeDetails.synopsis.isNotEmpty()) {
            SharableDescription(
                title = Constant.SYNOPSIS,
                description = animeDetails.synopsis,
                onClickShare = onClickShare
            )
        }
        if (animeDetails.background.isNotEmpty()) {
            SharableDescription(
                title = Constant.BACKGROUND,
                description = animeDetails.background,
                onClickShare = onClickShare
            )
        }
    }
}

@Composable
private fun ExpandableTitles(
    synonym: String,
    japanese: String,
    english: String
) {
    var isExpanded by remember { mutableStateOf(false) }

    val items = listOf(
        Constant.SYNONYM to synonym,
        Constant.JAPANESE to japanese,
        Constant.ENGLISH to english
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ExpandableItem(title = items[0].first, items[0].second)

        AnimatedVisibility(visible = isExpanded) {
            Column {
                items.drop(1).forEach { (title, desc) ->
                    ExpandableItem(title = title, description = desc)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.size(24.dp))
            Text(
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                text = if (isExpanded) Constant.LESS_TITLE else Constant.MORE_TITLE
            )
            Icon(
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
                imageVector = if (isExpanded) CustomIcon.ROUND_ARROW_DROP_DOWN else CustomIcon.ROUND_ARROW_DROP_UP,
                contentDescription = Constant.EXPAND_ICON
            )
        }
    }
}

@Composable
private fun SharableDescription(
    title: String,
    description: String,
    onClickShare: () -> Unit
) = Column {
    Row(
        modifier = Modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.width(24.dp))
        Text(
            modifier = Modifier.weight(1F),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = title
        )
        Icon(
            modifier = Modifier.clickable(onClick = onClickShare),
            tint = MaterialTheme.colorScheme.primary,
            imageVector = CustomIcon.FILL_SHARE,
            contentDescription = Constant.SHARE_ICON

        )
    }
    Text(
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        text = description
    )
}


@Composable
private fun ExpandableItem(title: String, description: String) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleSmall,
        text = title
    )
    Text(
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyMedium,
        text = description
    )
    Spacer(Modifier.height(8.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailsTabComponentPreview() {
    DetailsTabComponent(animeDetails = AnimeDetails(), onClickShare = {})
}
