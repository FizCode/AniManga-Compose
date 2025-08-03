package dev.fizcode.designsystem.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.util.Constant

@Composable
fun EmptyCardComponent(
    modifier: Modifier = Modifier,
    desc: String = ""
) = Column(
    modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp),
        painter = painterResource(id = Constant.IMG_EMPTY_PATH),
        contentDescription = Constant.IMG_RESERVED_DESC
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
private fun EmptyComponentPreview() = Column {
    EmptyCardComponent(
        desc = "You have no bookmarked media yet!"
    )
}