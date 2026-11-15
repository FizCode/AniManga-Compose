package dev.fizcode.designsystem.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.fizcode.designsystem.animation.AnimangaSearchIllustration

/**
 * Placeholder of a search that has not been made yet: the animated search illustration, centered
 * (both axes, it fills the available space), with an optional description below it.
 *
 * @param modifier modifier applied to the root column.
 * @param desc message shown under the illustration, nothing is rendered when empty.
 * @see EmptyCardComponent for a search that found nothing.
 */
@Composable
fun SearchPromptCardComponent(
    modifier: Modifier = Modifier,
    desc: String = ""
) = Column(
    modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    AnimangaSearchIllustration(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp)
            .aspectRatio(1f)
    )
    if (desc.isNotEmpty()) Text(
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        text = desc
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchPromptCardComponentPreview() = Column {
    SearchPromptCardComponent(
        desc = "Search your favorite anime."
    )
}
