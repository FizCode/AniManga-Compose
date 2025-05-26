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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.util.Constant.Component
import dev.fizcode.designsystem.util.base.shimmerBrush

/**
 * TODO: Add description
 */
@Composable
fun MovieCardSimpleShimmer() {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .width(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f))
                        .fillMaxWidth()
                        .height(178.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = Component.CARD_IMAGE,
                    model = null
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(shimmerBrush(targetValue = 1300f)),
                text = ""
            )
        }
    }
}

/**
 * TODO: Add description
 */
@Composable
fun MovieCardSmallShimmer() {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(shimmerBrush(targetValue = 1300f))
                        .size(100.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = Component.CARD_IMAGE,
                    model = null
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f)),
                    text = ""
                )
                Text(
                    modifier = Modifier
                        .width(150.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f)),
                    text = ""
                )
                Text(
                    modifier = Modifier
                        .width(100.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f)),
                    text = ""
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f)),
                    text = ""
                )
            }
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
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .width(328.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f))
                        .width(114.dp)
                        .height(164.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = Component.CARD_IMAGE,
                    model = null
                )
            }
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f)),
                    text = ""
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    modifier = Modifier
                        .width(170.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f)),
                    text = ""
                )
                Text(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .width(140.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f)),
                    text = ""
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f)),
                    text = ""
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f)),
                    text = ""
                )
            }
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
