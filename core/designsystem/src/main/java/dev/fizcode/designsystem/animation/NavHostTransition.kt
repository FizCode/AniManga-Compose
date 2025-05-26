package dev.fizcode.designsystem.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import dev.fizcode.designsystem.util.Constant.Animation

fun navHostEnterTransition(): EnterTransition =
    scaleIn(
        animationSpec = tween(
            durationMillis = Animation.INT_100,
            delayMillis = Animation.INT_35
        ),
        initialScale = Animation.FLOAT_1_1F,
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = Animation.INT_100,
            delayMillis = Animation.INT_35
        ),
    )


fun navHostExitTransition(): ExitTransition =
    scaleOut(
        targetScale = Animation.FLOAT_0_9F,
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = Animation.INT_35,
            easing = CubicBezierEasing(
                a = Animation.FLOAT_0_1F,
                b = Animation.FLOAT_0_1F,
                c = Animation.FLOAT_0_1F,
                d = Animation.FLOAT_0_1F
            )
        ),
    )
