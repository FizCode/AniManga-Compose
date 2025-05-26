package dev.fizcode.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon.ROUND_STAR_RATE
import dev.fizcode.designsystem.theme.YellowGold
import dev.fizcode.designsystem.util.Constant.Component

@Composable
internal fun RatingChip(
    rating: String
) {
    Row(
        Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(top = 2.dp, bottom = 2.dp),
    ) {
        Spacer(Modifier.width(8.dp))
        Text(
            color = Color.Black,
            style = MaterialTheme.typography.labelMedium,
            text = rating
        )
        Icon(
            modifier = Modifier.size(16.dp),
            tint = YellowGold,
            imageVector = ROUND_STAR_RATE,
            contentDescription = Component.CHIP_ICON
        )
        Spacer(Modifier.width(4.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun RatingChipPreview() {
    RatingChip(rating = "8.50")
}
