package dev.fizcode.mediadetails.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.util.base.shimmer
import dev.fizcode.mediadetails.util.Constant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImagesBottomSheet(
    imageUrl: String,
    onDismiss: () -> Unit
) = ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    ImageContent(imageUrl = imageUrl)
}

/** Sheet body, kept separate so it can be previewed (the sheet itself renders in a dialog window). */
@Composable
private fun ImageContent(imageUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmer(),
            contentScale = ContentScale.Fit
        )
        TextButton(
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = {}
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ImagePreview() {
    AniMangaAnimeAndMangaInfoTheme {
        Surface(
            shape = BottomSheetDefaults.ExpandedShape,
            color = BottomSheetDefaults.ContainerColor
        ) {
            ImageContent(imageUrl = "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg")
        }
    }
}
