package dev.fizcode.designsystem.component.other

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon

@Composable
fun FiveStarReview() {
    LazyRow() {
        items(count = 5) {
            Icon(
                modifier = Modifier
                    .size(16.dp),
                imageVector = CustomIcon.ROUND_STAR_RATE,
                contentDescription = ""
            )
        }
    }
}
