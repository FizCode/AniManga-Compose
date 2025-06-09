package dev.fizcode.designsystem.component.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.util.base.shimmerBrush

/**
 * TODO: Add description
 */
@Composable
fun MovieCardSimpleShimmer() {
    val clipRounded8 = RoundedCornerShape(8.dp)
    val shimmer = shimmerBrush()
    Column(
        modifier = Modifier
            .clip(clipRounded8)
            .width(140.dp)
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(clipRounded8)
                .background(shimmer)
                .fillMaxWidth()
                .height(178.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(clipRounded8)
                .background(shimmer)
        )
    }
}

/**
 * TODO: Add description
 */
@Composable
fun MovieCardSmallShimmer() {
    val clipRounded8 = RoundedCornerShape(8.dp)
    val shimmer = shimmerBrush()
    Row(
        modifier = Modifier
            .clip(clipRounded8)
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(shimmer)
                .size(100.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(clipRounded8)
                    .background(shimmer)
            )
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(16.dp)
                    .clip(clipRounded8)
                    .background(shimmer)
            )
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(16.dp)
                    .clip(clipRounded8)
                    .background(shimmer)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(clipRounded8)
                    .background(shimmer)
            )
        }
    }
}

/**
 * TODO: Add description
 */
@Composable
fun MovieCardLargeShimmer(
    modifier: Modifier = Modifier,
) {
    val clipRounded8 = RoundedCornerShape(8.dp)
    val shimmer = shimmerBrush()
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .width(328.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(clipRounded8)
                .background(shimmer)
                .width(114.dp)
                .height(164.dp)
        )
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(clipRounded8)
                    .background(shimmer)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .width(170.dp)
                    .height(18.dp)
                    .clip(clipRounded8)
                    .background(shimmer)
            )
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .width(140.dp)
                    .height(16.dp)
                    .clip(clipRounded8)
                    .background(shimmer)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(clipRounded8)
                    .background(shimmer)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(clipRounded8)
                    .background(shimmer)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCardShimmerPreview() {
    Column {
        MovieCardSimpleShimmer()
        MovieCardSmallShimmer()
        MovieCardLargeShimmer()
    }
}
