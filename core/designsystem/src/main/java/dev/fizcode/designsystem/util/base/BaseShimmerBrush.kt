package dev.fizcode.designsystem.util.base

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Creates a shimmering brush effect using an animated linear gradient.
 *
 * This function provides a shimmer effect commonly used as a placeholder for loading UI elements.
 * When [showShimmer] is true, it returns a [Brush.linearGradient] with animated shimmer colors.
 * When false, it returns a transparent brush.
 *
 * The shimmer animation transitions the gradient diagonally using [rememberInfiniteTransition].
 * The shimmer colors fade in and out with light gray shades for a smooth effect.
 *
 * @param showShimmer A flag indicating whether to show the shimmer effect. Defaults to true.
 * @param targetValue The target offset value for the shimmer animation. Controls the shimmer distance. Defaults to 1000f.
 *
 * @return A [Brush] that can be used for background painting with shimmer animation or transparent fallback.
 *
 * Example usage:
 * ```kotlin
 * Box(
 *     modifier = Modifier
 *         .size(100.dp)
 *         .background(brush = shimmerBrush())
 * )
 * ```
 */
@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "Transition")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            ), label = "Translate Animation"
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}
