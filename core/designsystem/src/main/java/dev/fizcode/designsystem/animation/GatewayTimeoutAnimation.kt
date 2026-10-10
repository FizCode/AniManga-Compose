package dev.fizcode.designsystem.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.VectorComposable
import androidx.compose.ui.graphics.vector.VectorConfig
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.illustration.gatewayTimeout

private const val FLOATING_TEXT_GROUP = "floatingText"

/** Distance, in viewport units (the illustration is 500 x 500), the "504 error" text drifts up and down. */
private const val FLOAT_DISTANCE = 8f
private const val FLOAT_DURATION_MILLIS = 1800

private const val PANIC_MARKS_GROUP = "panicMarks"
private const val PANIC_CYCLE_MILLIS = 2400

private const val GEAR_ROTATION_MILLIS = 12_000
private const val GEAR_SIDE_COLOR = 0xFF37474FL

/** The gears are drawn in perspective: a flat gear is squashed vertically by this factor. */
private const val GEAR_SQUASH = 0.575f

/**
 * A gear of the illustration. [topGroup] names the group holding its top face, [pivotX] and [pivotY]
 * the centre of that face, [thickness] how far the side wall extends below it, and [shadowEnd] how far
 * its shadow reaches below the face, in viewport units.
 */
private class Gear(
    val group: String,
    val topGroup: String,
    val shadowGroup: String,
    val pivotX: Float,
    val pivotY: Float,
    val thickness: Float,
    val shadowEnd: Float,
    val clockwise: Boolean
)

private val LeftGear = Gear("leftGear", "leftGearTop", "leftGearShadow", 107f, 365f, 11f, 17f, clockwise = true)
private val RightGear = Gear("rightGear", "rightGearTop", "rightGearShadow", 190.5f, 326f, 9f, 16f, clockwise = false)
private val GearsByGroup = listOf(LeftGear, RightGear).associateBy { it.group }
private val GearsByShadowGroup = listOf(LeftGear, RightGear).associateBy { it.shadowGroup }
private const val GEAR_SHADOW_COLOR = 0xFFE0E0E0L
private const val GEAR_LAYER_STEP = 0.5f

/**
 * The gateway timeout illustration with its "504 error gateway time out" text floating up and down,
 * the "!!" above the person blinking twice every few seconds as if they are panicking,
 * and its two gears turning in opposite directions (left one clockwise, right one counter-clockwise).
 */
@Composable
fun AnimangaGatewayTimeoutIllustration(
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val image = gatewayTimeout
    val offsetY by rememberInfiniteTransition(label = "gatewayTimeoutFloat").animateFloat(
        initialValue = -FLOAT_DISTANCE,
        targetValue = FLOAT_DISTANCE,
        animationSpec = infiniteRepeatable(
            animation = tween(FLOAT_DURATION_MILLIS, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatingTextOffsetY"
    )
    val gearAngle by rememberInfiniteTransition(label = "gatewayTimeoutGears").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(GEAR_ROTATION_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gearAngle"
    )
    val panicAlpha by rememberInfiniteTransition(label = "gatewayTimeoutPanic").animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = PANIC_CYCLE_MILLIS
                1f at 0
                1f at 100
                0f at 126
                0f at 214
                1f at 240
                1f at 334
                0f at 360
                0f at 446
                1f at 474
            }
        ),
        label = "panicMarksAlpha"
    )
    val floatingTextConfig = object : VectorConfig {
        @Suppress("UNCHECKED_CAST")
        override fun <T> getOrDefault(property: VectorProperty<T>, defaultValue: T): T =
            if (property == VectorProperty.TranslateY) offsetY as T else defaultValue
    }
    val painter = rememberVectorPainter(
        defaultWidth = image.defaultWidth,
        defaultHeight = image.defaultHeight,
        viewportWidth = image.viewportWidth,
        viewportHeight = image.viewportHeight,
        name = image.name,
        autoMirror = image.autoMirror
    ) { _, _ ->
        val configs = mapOf(FLOATING_TEXT_GROUP to floatingTextConfig)
        val gearFaces = GearsByGroup.mapValues { (_, gear) -> image.root.gearFace(gear) }
        image.root.forEach { node ->
            when {
                node is VectorGroup && node.name in GearsByGroup -> {
                    val gear = GearsByGroup.getValue(node.name)
                    SpinningGear(node, gear, gearFaces[gear.group], gearAngle)
                }

                node is VectorGroup && node.name in GearsByShadowGroup -> {
                    val gear = GearsByShadowGroup.getValue(node.name)
                    val face = gearFaces[gear.group]
                    if (face == null) RenderVectorGroup(node) else SpinningGearShadow(gear, face, gearAngle)
                }

                node is VectorGroup && node.name == PANIC_MARKS_GROUP -> BlinkingMarks(node, panicAlpha)
                node is VectorGroup -> RenderConfiguredGroup(node, configs)
                node is VectorPath -> RenderVectorPath(node)
            }
        }
    }

    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = contentDescription
    )
}

private fun VectorGroup.gearFace(gear: Gear): VectorPath? {
    val gearGroup = firstOrNull { it is VectorGroup && it.name == gear.group } as? VectorGroup
    val top = gearGroup?.firstOrNull { it is VectorGroup && it.name == gear.topGroup } as? VectorGroup
    return top?.filterIsInstance<VectorPath>()?.firstOrNull()
}

/**
 * Draws [gear] turned by [angle] degrees. Only its top face turns: the side wall is rebuilt from
 * copies of that face stacked downwards, so it keeps following the teeth.
 */
@Composable
@VectorComposable
private fun SpinningGear(group: VectorGroup, gear: Gear, face: VectorPath?, angle: Float) {
    if (face == null) return RenderVectorGroup(group)
    GearLayers(gear, face, angle, from = gear.thickness, to = 0f, sideColor = GEAR_SIDE_COLOR)
}

/** The shadow of [gear] on the floor: the part of the stacked silhouette that lies beyond its side wall. */
@Composable
@VectorComposable
private fun SpinningGearShadow(gear: Gear, face: VectorPath, angle: Float) {
    GearLayers(gear, face, angle, from = gear.shadowEnd, to = gear.thickness, sideColor = GEAR_SHADOW_COLOR)
}

/**
 * Stacks copies of [face], from [from] down to [to] (viewport units below the top face), turned
 * by [angle]. The copy at 0 keeps the face colours, the others use [sideColor].
 *
 * The face is drawn in perspective, so the rotation happens on the un-squashed shape and is
 * squashed back afterwards.
 */
@Composable
@VectorComposable
private fun GearLayers(gear: Gear, face: VectorPath, angle: Float, from: Float, to: Float, sideColor: Long) {
    val sideBrush = SolidColor(Color(sideColor))
    val rotation = if (gear.clockwise) angle else -angle

    var offset = from
    while (offset >= to) {
        Group(
            pivotX = gear.pivotX,
            pivotY = gear.pivotY,
            scaleY = GEAR_SQUASH,
            translationY = offset
        ) {
            Group(
                pivotX = gear.pivotX,
                pivotY = gear.pivotY,
                rotation = rotation,
                scaleY = 1f / GEAR_SQUASH
            ) {
                Path(
                    pathData = face.pathData,
                    pathFillType = face.pathFillType,
                    fill = if (offset == 0f) face.fill else sideBrush
                )
            }
        }
        offset -= GEAR_LAYER_STEP
    }
}

/**
 * Draws [group] with the overrides from [configs], which [RenderVectorGroup] only applies to the
 * groups nested inside the one it is given, never to that group itself.
 */
@Composable
@VectorComposable
private fun RenderConfiguredGroup(group: VectorGroup, configs: Map<String, VectorConfig>) {
    val translationY = configs[group.name]
        ?.getOrDefault(VectorProperty.TranslateY, group.translationY)
        ?: group.translationY
    Group(
        name = group.name,
        rotation = group.rotation,
        pivotX = group.pivotX,
        pivotY = group.pivotY,
        scaleX = group.scaleX,
        scaleY = group.scaleY,
        translationX = group.translationX,
        translationY = translationY,
        clipPathData = group.clipPathData
    ) {
        RenderVectorGroup(group, configs)
    }
}

@Composable
@VectorComposable
private fun RenderVectorPath(path: VectorPath, alpha: Float = 1f) = Path(
    pathData = path.pathData,
    pathFillType = path.pathFillType,
    fill = path.fill,
    fillAlpha = path.fillAlpha * alpha
)

/** Draws the paths of [group] with their opacity multiplied by [alpha]. */
@Composable
@VectorComposable
private fun BlinkingMarks(group: VectorGroup, alpha: Float) {
    group.forEach { node ->
        if (node is VectorPath) RenderVectorPath(node, alpha) else if (node is VectorGroup) RenderVectorGroup(node)
    }
}

@Composable
@Preview(showBackground = true)
private fun AnimangaGatewayTimeoutIllustrationPreview() {
    AnimangaGatewayTimeoutIllustration(modifier = Modifier.size(300.dp))
}
