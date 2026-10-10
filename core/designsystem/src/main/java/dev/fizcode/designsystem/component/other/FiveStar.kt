package dev.fizcode.designsystem.component.other

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.theme.YellowGold
import dev.fizcode.designsystem.util.Constant.Component

/**
 * Five-star rating row rendered from a 10-point score.
 *
 * The score is halved and clamped to 0..5, then drawn as full, half (fraction in [0.25, 0.75))
 * and empty stars.
 *
 * @param score rating on a 0..10 scale, e.g. a MyAnimeList score.
 */
@Composable
fun FiveStarReview(
    score: Double,
) {
    val starRating = (score / 2).coerceIn(0.0, 5.0)
    val fullStars = starRating.toInt()
    val hasHalfStar = starRating - fullStars in 0.25..<0.75
    val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

    Row {
        repeat(fullStars) {
            Icon(
                modifier = Modifier.size(16.dp),
                tint = YellowGold,
                imageVector = CustomIcon.ROUND_STAR_RATE,
                contentDescription = Component.FULL_STAR
            )
        }

        if (hasHalfStar) {
            Icon(
                modifier = Modifier.size(16.dp),
                tint = YellowGold,
                imageVector = CustomIcon.ROUND_STAR_HALF,
                contentDescription = Component.HALF_STAR
            )
        }

        repeat(emptyStars) {
            Icon(
                modifier = Modifier.size(16.dp),
                tint = YellowGold,
                imageVector = CustomIcon.ROUND_STAR_BORDER,
                contentDescription = Component.EMPTY_STAR
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun FiveStarReviewPreview() {
    Column {
        FiveStarReview(score = 10.0)
        FiveStarReview(score = 7.5)
        FiveStarReview(score = 5.0)
        FiveStarReview(score = 0.0)
    }
}
