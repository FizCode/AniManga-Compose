package dev.fizcode.designsystem.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import dev.fizcode.designsystem.illustration.thankYou
import kotlin.math.PI
import kotlin.math.sin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TEXT_GROUP = "thankYouText"

private const val TEXT_PIVOT_X = 181f
private const val TEXT_PIVOT_Y = 38f

private const val TEXT_START_SCALE = 0.2f
private const val TEXT_DELAY_MILLIS = 250L
private const val TEXT_FADE_MILLIS = 120

private const val WAVE_MILLIS = 3200

/** Where the strings of the balloons on each side of the person meet the floor, in viewport units. */
private const val LEFT_STRINGS_X = 97.3f
private const val LEFT_STRINGS_Y = 211f
private const val RIGHT_STRINGS_X = 154.7f
private const val RIGHT_STRINGS_Y = 219.5f

/**
 * A heart balloon of the illustration, with its string. It leans around the point where its string
 * meets the floor ([pivotX], [pivotY]), up to [swing] degrees to each side. [phase] is how far ahead
 * of the others it is, as a fraction of the wave, so the balloons do not move as one block.
 *
 * The shorter the string, the larger the swing, so every balloon travels about the same distance.
 */
private class Balloon(
    val group: String,
    val pivotX: Float,
    val pivotY: Float,
    val swing: Float,
    val phase: Float
)

private val BalloonsByGroup = listOf(
    Balloon("balloonLeftTop", LEFT_STRINGS_X, LEFT_STRINGS_Y, swing = 3f, phase = 0f),
    Balloon("balloonLeftMiddle", LEFT_STRINGS_X, LEFT_STRINGS_Y, swing = 3.5f, phase = 0.05f),
    Balloon("balloonLeftBottom", LEFT_STRINGS_X, LEFT_STRINGS_Y, swing = 4.5f, phase = 0.1f),
    Balloon("balloonRightTop", RIGHT_STRINGS_X, RIGHT_STRINGS_Y, swing = 2.8f, phase = 0.3f),
    Balloon("balloonRightMiddle", RIGHT_STRINGS_X, RIGHT_STRINGS_Y, swing = 3.5f, phase = 0.35f),
    Balloon("balloonRightBottom", RIGHT_STRINGS_X, RIGHT_STRINGS_Y, swing = 4.5f, phase = 0.4f)
).associateBy { it.group }

private val IllustrationAccent = SolidColor(Color(0xFF0061A4))

/**
 * The thank you illustration whose heart balloons keep waving left and right, and whose
 * "thank you!!!" text pops in with a small overshoot a moment after the illustration appears.
 *
 * The text pops in once: it does not repeat, and it does not play again when the illustration comes
 * back, for example after its pager page was scrolled away or the screen was rotated.
 *
 * @param accentColor replaces the illustration's blue accents, including their shaded and tinted variants.
 * Defaults to the theme's primary color.
 */
@Composable
fun AnimangaThankYouIllustration(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    val image = thankYou
    val wave by rememberInfiniteTransition(label = "thankYouBalloons").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(WAVE_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "balloonWave"
    )
    var popped by rememberSaveable { mutableStateOf(false) }
    val textScale = remember { Animatable(if (popped) 1f else TEXT_START_SCALE) }
    val textAlpha = remember { Animatable(if (popped) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (popped) return@LaunchedEffect
        delay(TEXT_DELAY_MILLIS)
        coroutineScope {
            launch {
                textScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch { textAlpha.animateTo(1f, tween(TEXT_FADE_MILLIS)) }
        }
        popped = true
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
                is VectorGroup -> {
                    val balloon = BalloonsByGroup[node.name]
                    when {
                        balloon != null -> TransformedGroup(
                            group = node,
                            accentColor = accentColor,
                            pivotX = balloon.pivotX,
                            pivotY = balloon.pivotY,
                            rotation = balloon.swing * sin(2 * PI * (wave + balloon.phase)).toFloat()
                        )

                        node.name == TEXT_GROUP -> TransformedGroup(
                            group = node,
                            accentColor = accentColor,
                            pivotX = TEXT_PIVOT_X,
                            pivotY = TEXT_PIVOT_Y,
                            scale = textScale.value,
                            alpha = textAlpha.value
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
    scale: Float = 1f,
    alpha: Float = 1f
) {
    Group(
        name = group.name,
        rotation = rotation,
        pivotX = pivotX,
        pivotY = pivotY,
        scaleX = scale,
        scaleY = scale
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
private fun AnimangaThankYouIllustrationPreview() {
    AnimangaThankYouIllustration(modifier = Modifier.size(300.dp))
}
