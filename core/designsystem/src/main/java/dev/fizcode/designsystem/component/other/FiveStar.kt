package dev.fizcode.designsystem.component.other

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.theme.YellowGold
import dev.fizcode.designsystem.util.Constant.Component

@Composable
fun FiveStarReview(
    score: Double,
) {
    val starRating = (score / 2).coerceIn(0.0, 5.0)
    val fullStars = starRating.toInt()
    val hasHalfStar = (starRating - fullStars) >= 0.25 && (starRating - fullStars) < 0.75
    val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

    LazyRow {
        items(fullStars) {
            Icon(
                modifier = Modifier.size(16.dp),
                tint = YellowGold,
                imageVector = CustomIcon.ROUND_STAR_RATE,
                contentDescription = Component.FULL_STAR
            )
        }

        if (hasHalfStar) {
            item {
                Icon(
                    modifier = Modifier.size(16.dp),
                    tint = YellowGold,
                    imageVector = CustomIcon.ROUND_STAR_HALF,
                    contentDescription = Component.HALF_STAR
                )
            }
        }

        items(emptyStars) {
            Icon(
                modifier = Modifier.size(16.dp),
                tint = YellowGold,
                imageVector = CustomIcon.ROUND_STAR_BORDER,
                contentDescription = Component.EMPTY_STAR
            )
        }
    }
}

