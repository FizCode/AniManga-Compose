package dev.fizcode.designsystem.component.other

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.util.Constant

@Composable
fun SearchSimpleRowComponent(
    modifier: Modifier = Modifier,
    value: String,
    onClickSearch: () -> Unit
) = Row(
    modifier = modifier
        .clip(RoundedCornerShape(100.dp))
        .clickable { onClickSearch() }
        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
        .padding(vertical = 8.dp, horizontal = 18.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    Icon(
        tint = MaterialTheme.colorScheme.secondary,
        imageVector = CustomIcon.FILL_SEARCH,
        contentDescription = Constant.SEARCH_ICON
    )
    Text(
        color = MaterialTheme.colorScheme.outline,
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        text = value
    )
}
