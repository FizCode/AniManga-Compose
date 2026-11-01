package dev.fizcode.mediadetails.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.designsystem.util.base.shimmer
import dev.fizcode.mediadetails.presentation.model.ImageGallery
import dev.fizcode.mediadetails.util.Constant
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

/**
 * Bottom sheet with the pictures of a media. With more than one picture the user can swipe between
 * them, the dots show the current one and [onDownload] gets the url of the one on screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImagesBottomSheet(
    gallery: ImageGallery,
    onDownload: (imageUrl: String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = gallery.initialIndex.coerceIn(0, gallery.urls.lastIndex.coerceAtLeast(0))
    ) { gallery.urls.size }
    var showConfirm by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        ImageContent(
            gallery = gallery,
            pagerState = pagerState,
            onDownload = { showConfirm = true }
        )
    }
    if (showConfirm) {
        DownloadConfirmDialog(
            onConfirm = {
                showConfirm = false
                onDownload(gallery.urls[pagerState.currentPage])
                // Slide the sheet away first, then drop it from the composition.
                scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
            },
            onDismiss = { showConfirm = false }
        )
    }
}

@Composable
private fun DownloadConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) = AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = Constant.DOWNLOAD_DIALOG_TITLE) },
    text = { Text(text = Constant.DOWNLOAD_DIALOG_MESSAGE) },
    confirmButton = {
        TextButton(onClick = onConfirm) { Text(text = Constant.DOWNLOAD) }
    },
    dismissButton = {
        TextButton(onClick = onDismiss) { Text(text = Constant.CANCEL) }
    }
)

@Preview(showBackground = true)
@Composable
private fun DownloadConfirmDialogPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        DownloadConfirmDialog(onConfirm = {}, onDismiss = {})
    }
}

/** Sheet body, kept separate so it can be previewed (the sheet itself renders in a dialog window). */
@Composable
private fun ImageContent(
    gallery: ImageGallery,
    pagerState: PagerState,
    onDownload: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            key = { gallery.urls[it] },
            pageSpacing = 16.dp
        ) { page ->
            SheetImage(imageUrl = gallery.urls[page])
        }
        if (gallery.urls.size > 1) {
            PageDots(
                count = gallery.urls.size,
                current = pagerState.currentPage,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        TextButton(
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = onDownload
        ) {
            Icon(
                imageVector = CustomIcon.ROUND_DOWNLOAD,
                contentDescription = Constant.DOWNLOAD_ICON
            )
            Spacer(Modifier.width(8.dp))
            Text(text = Constant.DOWNLOAD)
        }
    }
}

@Composable
private fun SheetImage(imageUrl: String) {
    var isLoading by remember(imageUrl) { mutableStateOf(true) }
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        // While loading, the poster ratio keeps the space (a placeholder has no size of its own)
        // and the shimmer runs; once the image is there both are dropped.
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .then(if (isLoading) Modifier.aspectRatio(2f / 3f) else Modifier)
            .clip(RoundedCornerShape(12.dp))
            .then(if (isLoading) Modifier.shimmer() else Modifier),
        placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceContainerHighest),
        error = ColorPainter(MaterialTheme.colorScheme.surfaceContainerHighest),
        onLoading = { isLoading = true },
        onSuccess = { isLoading = false },
        onError = { isLoading = false },
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun PageDots(
    count: Int,
    current: Int,
    modifier: Modifier = Modifier
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    repeat(count) { index ->
        Spacer(
            Modifier
                .height(6.dp)
                .width(
                    animateDpAsState(
                        if (index == current) 22.dp else 6.dp,
                        label = "dotWidth"
                    ).value
                )
                .background(
                    color = if (index == current) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                    shape = CircleShape
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ImagesBottomSheetPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        Surface(
            shape = BottomSheetDefaults.ExpandedShape,
            color = BottomSheetDefaults.ContainerColor
        ) {
            val gallery = ImageGallery(
                urls = persistentListOf(
                    "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg",
                    "https://cdn.myanimelist.net/images/anime/1015/138006.jpg",
                    "https://cdn.myanimelist.net/images/anime/1015/138006t.jpg"
                )
            )
            ImageContent(
                gallery = gallery,
                pagerState = rememberPagerState { gallery.urls.size },
                onDownload = {}
            )
        }
    }
}
