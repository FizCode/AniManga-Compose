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

@Composable
internal fun DetailsTabComponent() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExpandableTitles()
        SharableDescription(
            title = "Synopsis",
            description = "Tired of ceaseless war destroying humans, spirits, and demons alike, the Demon King of Tyranny, Anos Voldigoad, reincarnates to restore peace. Two thousand years have passed...but who could have known that his descendants' complacency would cause magic to regress so much? After receiving an invitation to the illustrious Demon King Academy, a school tasked with locating the reincarnated founding ancestor, Anos resolves to enroll, only to discover that his magic is off the charts—literally! With power too great to be measured, the former Demon King is branded a misfit. How will he convince demonkind that the school's founder stands before them? Thus begins a misfit's climb to the top of the demon hierarchy!\n(Source: J-Novel Club)",
            onClickShare = {}
        )
        SharableDescription(
            title = "Background",
            description = "Maou Gakuin no Futekigousha: Shijou Saikyou no Maou no Shiso, Tensei shite Shison-tachi no Gakkou e Kayou has been published digitally in English as The Misfit of Demon King Academy by J-Novel Club since June 27, 2022, and in print by Yen Press under the J-Novel Club imprint since August 1, 2023.",
            onClickShare = {}
        )
    }
}

@Composable
private fun ExpandableTitles() {
    var isExpanded by remember { mutableStateOf(false) }

    val items = listOf(
        "Synonym" to "Maou Gakuin no Futekigousha 2nd Season, The Misfit of Demon King Academy 2nd Season",
        "Japanese" to "魔王学院の不適合者 II ～史上最強の魔王の始祖、転生して子孫たちの学校へ通う～ 第2クール",
        "English" to "The Misfit of Demon King Academy II Part 2"
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
            Text(
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                text = if (isExpanded) "Less Title" else "More Title"
            )
            Icon(
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
                imageVector = if (isExpanded) CustomIcon.ROUND_ARROW_DROP_DOWN else CustomIcon.ROUND_ARROW_DROP_UP,
                contentDescription = "Expand Icon"
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
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(start = 24.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = title
        )
        Icon(
            modifier = Modifier.clickable(onClick = onClickShare),
            tint = MaterialTheme.colorScheme.primary,
            imageVector = CustomIcon.FILL_SHARE,
            contentDescription = "Share"

        )
    }
    Text(
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
    DetailsTabComponent()
}
