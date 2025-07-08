package dev.fizcode.designsystem.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.util.Constant

@Composable
fun ReservedComponent(modifier: Modifier = Modifier) = Column(
    modifier = modifier
        .padding(16.dp)
        .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp),
        painter = painterResource(id = Constant.IMG_RESERVED),
        contentDescription = Constant.IMG_RESERVED_DESC
    )
    Text(
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        text = Constant.RESERVED_TXT
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReservedComponentPreview() = ReservedComponent()
