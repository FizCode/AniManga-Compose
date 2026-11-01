package dev.fizcode.mediadetails.presentation.header.anime

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
import dev.fizcode.designsystem.util.base.shimmer
import dev.fizcode.mediadetails.presentation.header.util.Constant
import dev.fizcode.mediadetails.presentation.model.ImageGallery
import kotlinx.collections.immutable.ImmutableList
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.presentation.MediaDetailsPreviewData
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SliderLargeImage(
    pictures: ImmutableList<String>,
    largePictures: ImmutableList<String>,
    onPictureClick: (ImageGallery) -> Unit
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
                    .shimmer()
                    .height(202.dp)
                    .clickable { onPictureClick(ImageGallery(largePictures.ifEmpty { pictures }, index)) },
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
                .shimmer()
                .clickable { onPictureClick(ImageGallery(largePictures.ifEmpty { pictures })) },
            contentScale = ContentScale.Crop,
            contentDescription = Constant.SLIDER_IMAGES,
            model = pictures.first()
        )
    }

    else -> {}
}

@Preview(showBackground = true)
@Composable
private fun SliderLargeImagePreview() {
    AniMangaAnimeAndMangaInfoTheme {
        SliderLargeImage(
            pictures = persistentListOf("https://example.com/1.jpg", "https://example.com/2.jpg"),
            largePictures = persistentListOf("https://example.com/1l.jpg"),
            onPictureClick = {}
        )
    }
}
