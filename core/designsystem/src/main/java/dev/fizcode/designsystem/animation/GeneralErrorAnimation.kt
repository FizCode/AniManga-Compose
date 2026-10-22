package dev.fizcode.designsystem.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.VectorComposable
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.illustration.generalError
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val MESSAGE_BUBBLE_GROUP = "messageBubble"

/** Tip of the bubble's tail, in viewport units (the illustration is 256 x 256): the bubble grows out of it. */
private const val BUBBLE_PIVOT_X = 104f
private const val BUBBLE_PIVOT_Y = 48f

private const val BUBBLE_START_SCALE = 0.2f
private const val BUBBLE_DELAY_MILLIS = 250L
private const val BUBBLE_FADE_MILLIS = 120

/** The blue of the illustration's message bubble, rug and accents, which [AnimangaGeneralErrorIllustration] replaces. */
private val IllustrationAccent = SolidColor(Color(0xFF0061A4))
private val IllustrationWhite = SolidColor(Color.White)

/**
 * The general error illustration where the "Sorry!" message bubble pops in once, growing out of its
 * tail with a small overshoot, a moment after the illustration appears. It does not repeat.
 *
 * @param accentColor replaces the illustration's blue accents, including their translucent tints.
 * Defaults to the theme's primary color.
 * @param onAccentColor color of the "Sorry!" text on the bubble. Defaults to the theme's on-primary color.
 */
@Composable
fun AnimangaGeneralErrorIllustration(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    onAccentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val image = generalError
    val bubbleScale = remember { Animatable(BUBBLE_START_SCALE) }
    val bubbleAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(BUBBLE_DELAY_MILLIS)
        coroutineScope {
            launch {
                bubbleScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch { bubbleAlpha.animateTo(1f, tween(BUBBLE_FADE_MILLIS)) }
        }
    }
    val painter = rememberVectorPainter(
        defaultWidth = image.defaultWidth,
        defaultHeight = image.defaultHeight,
        viewportWidth = image.viewportWidth,
        viewportHeight = image.viewportHeight,
        name = image.name,
        autoMirror = image.autoMirror
    ) { _, _ ->
        image.root.forEach { node ->
            when (node) {
                is VectorGroup if node.name == MESSAGE_BUBBLE_GROUP ->
                    PoppingBubble(
                        group = node,
                        accentColor = accentColor,
                        onAccentColor = onAccentColor,
                        scale = bubbleScale.value,
                        alpha = bubbleAlpha.value
                    )

                is VectorGroup -> RenderVectorGroup(node)
                is VectorPath -> RenderVectorPath(node, accentColor)
            }
        }
    }

    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = contentDescription
    )
}

/**
 * Draws [group] scaled by [scale] around the bubble's tail, with its opacity multiplied by [alpha].
 * The opaque white paths of the bubble are its text, drawn in [onAccentColor].
 */
@Composable
@VectorComposable
private fun PoppingBubble(
    group: VectorGroup,
    accentColor: Color,
    onAccentColor: Color,
    scale: Float,
    alpha: Float
) {
    Group(
        name = group.name,
        pivotX = BUBBLE_PIVOT_X,
        pivotY = BUBBLE_PIVOT_Y,
        scaleX = scale,
        scaleY = scale
    ) {
        group.forEach { node ->
            if (node is VectorPath) RenderVectorPath(
                node,
                accentColor,
                alpha,
                onAccentColor
            ) else if (node is VectorGroup) RenderVectorGroup(node)
        }
    }
}

@Composable
@VectorComposable
private fun RenderVectorPath(
    path: VectorPath,
    accentColor: Color,
    alpha: Float = 1f,
    textColor: Color? = null
) = Path(
    pathData = path.pathData,
    pathFillType = path.pathFillType,
    fill = when {
        path.fill == IllustrationAccent -> SolidColor(accentColor)
        textColor != null && path.fill == IllustrationWhite && path.fillAlpha == 1f -> SolidColor(
            textColor
        )

        else -> path.fill
    },
    fillAlpha = path.fillAlpha * alpha
)

@Composable
@Preview(showBackground = true)
private fun AnimangaGeneralErrorIllustrationPreview() {
    AnimangaGeneralErrorIllustration(modifier = Modifier.size(300.dp))
}
