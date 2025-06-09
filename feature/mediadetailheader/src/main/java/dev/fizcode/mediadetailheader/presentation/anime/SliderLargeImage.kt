package dev.fizcode.mediadetailheader.presentation.anime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetailheader.util.Constant
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SliderLargeImage(
    pictures: ImmutableList<String>,
    largePictures: ImmutableList<String>,
    onPictureClick: (String) -> Unit
) = when {
    pictures.size >= 2 -> {
        HorizontalMultiBrowseCarousel(
            state = rememberCarouselState { pictures.count() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 26.dp),
            preferredItemWidth = 280.dp,
            itemSpacing = 8.dp,
        ) { index ->
            val picture = pictures[index]
            AsyncImage(
                modifier = Modifier
                    .maskClip(MaterialTheme.shapes.small)
                    .background(shimmerBrush())
                    .height(202.dp)
                    .clickable { onPictureClick(largePictures[index]) },
                contentScale = ContentScale.Crop,
                contentDescription = Constant.SLIDER_IMAGES,
                model = picture
            )
        }
    }

    pictures.size == 1 -> {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(202.dp)
                .background(shimmerBrush())
                .clickable { onPictureClick(pictures.first()) },
            contentScale = ContentScale.Crop,
            contentDescription = Constant.SLIDER_IMAGES,
            model = pictures.first()
        )
    }

    else -> {}
}
