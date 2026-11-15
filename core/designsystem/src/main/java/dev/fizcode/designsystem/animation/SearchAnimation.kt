package dev.fizcode.designsystem.animation

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import dev.fizcode.designsystem.illustration.search

private const val RISE_DISTANCE = 16f
private const val RISE_MILLIS = 1000
private const val RISE_FADE_IN_MILLIS = 300

private const val RISEN_HOLD_MILLIS = 600
private const val RISEN_FADE_OUT_MILLIS = 300
private const val RISE_CYCLE_MILLIS = RISE_MILLIS + RISEN_HOLD_MILLIS + RISEN_FADE_OUT_MILLIS

private const val BUBBLE_MARK_GROUP = "bubbleQuestionMark"
private const val BOUNCE_CYCLE_MILLIS = 1500
private const val BOUNCE_HEIGHT = 4f

/**
 * A grey question mark around the person. ([pivotX], [pivotY]) is its centre and [tilt] how many
 * degrees it leans clockwise in the illustration: it rises upright and turns to that angle.
 * [start] is when in the loop it starts rising, as a fraction of the loop, so the marks take turns.
 */
private class QuestionMark(
    val group: String,
    val pivotX: Float,
    val pivotY: Float,
    val tilt: Float,
    val start: Float
) {
    private fun elapsedMillis(cycle: Float): Float = (cycle - start).mod(1f) * RISE_CYCLE_MILLIS

    fun rise(cycle: Float): Float =
        FastOutSlowInEasing.transform((elapsedMillis(cycle) / RISE_MILLIS).coerceAtMost(1f))

    fun alpha(cycle: Float): Float {
        val elapsed = elapsedMillis(cycle)
        val fadeOutStart = RISE_MILLIS + RISEN_HOLD_MILLIS
        return when {
            elapsed < RISE_FADE_IN_MILLIS -> elapsed / RISE_FADE_IN_MILLIS
            elapsed < fadeOutStart -> 1f
            else -> 1f - (elapsed - fadeOutStart) / RISEN_FADE_OUT_MILLIS
        }
    }
}

private val QuestionMarksByGroup = listOf(
    QuestionMark("questionMarkTopLeft", 58f, 74.5f, tilt = 11.5f, start = 0f),
    QuestionMark("questionMarkBottomRight", 180.6f, 208.2f, tilt = 16.4f, start = 1 / 6f),
    QuestionMark("questionMarkLeft", 48.5f, 117f, tilt = -8.2f, start = 2 / 6f),
    QuestionMark("questionMarkTopRight", 198.3f, 79f, tilt = -4.3f, start = 3 / 6f),
    QuestionMark("questionMarkBottomLeft", 88.7f, 199.8f, tilt = -8f, start = 4 / 6f),
    QuestionMark("questionMarkRight", 203.7f, 161.3f, tilt = -4.7f, start = 5 / 6f)
).associateBy { it.group }

private val IllustrationAccent = SolidColor(Color(0xFF0061A4))

/**
 * The search illustration whose grey question marks keep rising into place one after another, each
 * one starting upright below its place and turning to its tilt on the way up, and whose question
 * mark in the speech bubble hops every 1.5 seconds.
 *
 * @param accentColor replaces the illustration's blue accents. Defaults to the theme's primary color.
 */
@Composable
fun AnimangaSearchIllustration(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    val image = search
    val riseCycle by rememberInfiniteTransition(label = "searchQuestionMarks").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(RISE_CYCLE_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "questionMarksRiseCycle"
    )
    val bounce by rememberInfiniteTransition(label = "searchBubble").animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = BOUNCE_CYCLE_MILLIS
                0f at 0 using LinearOutSlowInEasing
                -BOUNCE_HEIGHT at 200 using FastOutLinearInEasing
                0f at 400 using LinearOutSlowInEasing
                -BOUNCE_HEIGHT / 3 at 510 using FastOutLinearInEasing
                0f at 620
                0f at BOUNCE_CYCLE_MILLIS
            }
        ),
        label = "bubbleQuestionMarkBounce"
    )
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
                is VectorGroup -> {
                    val questionMark = QuestionMarksByGroup[node.name]
                    when {
                        questionMark != null -> {
                            val rise = questionMark.rise(riseCycle)
                            TransformedGroup(
                                group = node,
                                accentColor = accentColor,
                                pivotX = questionMark.pivotX,
                                pivotY = questionMark.pivotY,
                                rotation = -questionMark.tilt * (1f - rise),
                                translationY = RISE_DISTANCE * (1f - rise),
                                alpha = questionMark.alpha(riseCycle)
                            )
                        }

                        node.name == BUBBLE_MARK_GROUP -> TransformedGroup(
                            group = node,
                            accentColor = accentColor,
                            translationY = bounce
                        )

                        else -> TransformedGroup(group = node, accentColor = accentColor)
                    }
                }

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

@Composable
@VectorComposable
private fun TransformedGroup(
    group: VectorGroup,
    accentColor: Color,
    pivotX: Float = 0f,
    pivotY: Float = 0f,
    rotation: Float = 0f,
    translationY: Float = 0f,
    alpha: Float = 1f
) {
    Group(
        name = group.name,
        rotation = rotation,
        pivotX = pivotX,
        pivotY = pivotY,
        translationY = translationY
    ) {
        group.forEach { node ->
            if (node is VectorPath) RenderVectorPath(node, accentColor, alpha)
            else if (node is VectorGroup) RenderVectorGroup(node)
        }
    }
}

@Composable
@VectorComposable
private fun RenderVectorPath(path: VectorPath, accentColor: Color, alpha: Float = 1f) = Path(
    pathData = path.pathData,
    pathFillType = path.pathFillType,
    fill = if (path.fill == IllustrationAccent) SolidColor(accentColor) else path.fill,
    fillAlpha = path.fillAlpha * alpha
)

@Composable
@Preview(showBackground = true)
private fun AnimangaSearchIllustrationPreview() {
    AnimangaSearchIllustration(modifier = Modifier.size(300.dp))
}
