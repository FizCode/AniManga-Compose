package dev.fizcode.designsystem.util.base

import android.provider.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val SHIMMER_PERIOD_MILLIS = 1200L
private val ShimmerBandWidth = 220.dp

/**
 * Fills the element with an animated shimmer, for loading skeletons.
 *
 * Every shimmer sweeps left to right in window coordinates and is driven by the frame clock, so all
 * shimmers on screen stay in sync as one continuous wave regardless of how many boxes a skeleton has.
 * The animation is read in the draw phase only, so it never triggers recomposition.
 * Colors come from [MaterialTheme], so it follows light and dark theme.
 *
 * Apply it after `clip` so the shimmer takes the clipped shape:
 * ```kotlin
 * Box(Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).shimmer())
 * ```
 */
@Composable
fun Modifier.shimmer(): Modifier {
    val surface = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    return this then ShimmerElement(
        baseColor = onSurface.copy(alpha = 0.08f).compositeOver(surface),
        highlightColor = onSurface.copy(alpha = 0.16f).compositeOver(surface),
    )
}

private data class ShimmerElement(
    val baseColor: Color,
    val highlightColor: Color,
) : ModifierNodeElement<ShimmerNode>() {
    override fun create() = ShimmerNode(baseColor, highlightColor)

    override fun update(node: ShimmerNode) {
        node.baseColor = baseColor
        node.highlightColor = highlightColor
        node.invalidateDraw()
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "shimmer"
    }
}

private class ShimmerNode(
    var baseColor: Color,
    var highlightColor: Color,
) : Modifier.Node(), DrawModifierNode, GlobalPositionAwareModifierNode,
    CompositionLocalConsumerModifierNode {
    private var progress = 0f
    private var windowOffset = Offset.Zero
    private var windowWidth = 0f
    private var job: Job? = null

    override fun onAttach() {
        val resolver = currentValueOf(LocalContext).contentResolver
        val animationsEnabled =
            Settings.Global.getFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) != 0f
        if (!animationsEnabled) return
        job = coroutineScope.launch {
            while (isActive) {
                // the frame time is shared by every node in the window, which keeps them in sync
                val frameNanos = withFrameNanos { it }
                val period = SHIMMER_PERIOD_MILLIS * 1_000_000L
                progress = (frameNanos % period).toFloat() / period
                invalidateDraw()
            }
        }
    }

    override fun onDetach() {
        job?.cancel()
        job = null
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        windowOffset = coordinates.positionInWindow()
        windowWidth = coordinates.findRootCoordinates().size.width.toFloat()
    }

    override fun ContentDrawScope.draw() {
        val band = ShimmerBandWidth.toPx()
        if (job == null || windowWidth == 0f) {
            drawRect(baseColor)
        } else {
            // the highlight travels from just off the left edge to just off the right edge of the window
            val startX = -band + (windowWidth + band) * progress - windowOffset.x
            drawRect(
                Brush.horizontalGradient(
                    colors = listOf(baseColor, highlightColor, baseColor),
                    startX = startX,
                    endX = startX + band,
                )
            )
        }
        drawContent()
    }
}
