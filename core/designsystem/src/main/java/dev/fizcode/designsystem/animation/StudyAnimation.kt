package dev.fizcode.designsystem.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
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
import dev.fizcode.designsystem.illustration.study
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val BAG_GROUP = "bag"
private const val CONSOLE_GROUP = "console"
private const val STUDENT_GROUP = "student"
private const val STUDENT_SHADOW_GROUP = "studentShadow"

/**
 * Distance, in viewport units (the illustration is 256 x 256), each part travels to reach its place.
 * The painter clips what leaves the viewport, and this keeps every part inside it from the start.
 */
private const val ENTER_DISTANCE = 48f
private const val ENTER_DELAY_MILLIS = 250L
private const val ENTER_STAGGER_MILLIS = 150L
private const val ENTER_FADE_MILLIS = 200

private val IllustrationAccent = SolidColor(Color(0xFF0061A4))

private class Entrance(entered: Boolean) {
    val offset = Animatable(if (entered) 0f else ENTER_DISTANCE)
    val alpha = Animatable(if (entered) 1f else 0f)

    suspend fun play(delayMillis: Long) = coroutineScope {
        delay(delayMillis)
        launch {
            offset.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch { alpha.animateTo(1f, tween(ENTER_FADE_MILLIS)) }
    }
}

/**
 * The study illustration whose parts slide and fade into place one after another, a moment after the
 * illustration appears: the bag from the top, the console and its controller from the left, then the
 * student with the laptop and the books they sit on from the right.
 *
 * It plays once: it does not repeat, and it does not play again when the illustration comes back,
 * for example after its pager page was scrolled away or the screen was rotated.
 *
 * @param accentColor replaces the illustration's blue accents, including their shaded and tinted variants.
 * Defaults to the theme's primary color.
 */
@Composable
fun AnimangaStudyIllustration(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    val image = study
    var entered by rememberSaveable { mutableStateOf(false) }
    val bag = remember { Entrance(entered) }
    val console = remember { Entrance(entered) }
    val student = remember { Entrance(entered) }
    LaunchedEffect(Unit) {
        if (entered) return@LaunchedEffect
        coroutineScope {
            launch { bag.play(ENTER_DELAY_MILLIS) }
            launch { console.play(ENTER_DELAY_MILLIS + ENTER_STAGGER_MILLIS) }
            launch { student.play(ENTER_DELAY_MILLIS + ENTER_STAGGER_MILLIS * 2) }
        }
        entered = true
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
                is VectorGroup -> when (node.name) {
                    BAG_GROUP -> EnteringGroup(
                        group = node,
                        accentColor = accentColor,
                        translationY = -bag.offset.value,
                        alpha = bag.alpha.value
                    )

                    CONSOLE_GROUP -> EnteringGroup(
                        group = node,
                        accentColor = accentColor,
                        translationX = -console.offset.value,
                        alpha = console.alpha.value
                    )

                    STUDENT_GROUP, STUDENT_SHADOW_GROUP -> EnteringGroup(
                        group = node,
                        accentColor = accentColor,
                        translationX = student.offset.value,
                        alpha = student.alpha.value
                    )

                    else -> EnteringGroup(group = node, accentColor = accentColor)
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
private fun EnteringGroup(
    group: VectorGroup,
    accentColor: Color,
    translationX: Float = 0f,
    translationY: Float = 0f,
    alpha: Float = 1f
) {
    Group(
        name = group.name,
        translationX = translationX,
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
private fun AnimangaStudyIllustrationPreview() {
    AnimangaStudyIllustration(modifier = Modifier.size(300.dp))
}
