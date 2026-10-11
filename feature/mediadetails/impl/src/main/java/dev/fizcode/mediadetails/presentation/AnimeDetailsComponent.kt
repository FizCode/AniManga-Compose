package dev.fizcode.mediadetails.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.other.ErrorWithRetry
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetails.presentation.header.DetailHeaderComponent
import dev.fizcode.mediadetails.presentation.info.model.AnimeCastUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.presentation.info.DetailInfoComponent
import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import dev.fizcode.mediadetails.util.Constant
import kotlinx.collections.immutable.ImmutableList
import dev.fizcode.common.settings.MediaTitles

@Composable
internal fun AnimeDetailsComponent(
    modifier: Modifier = Modifier,
    isBookmarked: Boolean,
    animeDetails: UiState<AnimeDetailsUiModel>,
    animeCast: UiState<ImmutableList<AnimeCastUiModel>>,
    animeStaff: UiState<ImmutableList<AnimeStaffUiModel>>,
    headerTitle: (MediaTitles) -> Unit,
    selectedImage: (String) -> Unit,
    onRetryDetails: () -> Unit,
    onRetryCastAndStaff: () -> Unit,
    onClickBookmark: (AnimeDetailsUiModel) -> Unit,
    onClickShare: (String) -> Unit
) = LazyColumn(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    when (animeDetails) {
        is UiState.Success -> {
            headerTitle(animeDetails.data.animeDetailsHeaderUiModel.titles)
            item {
                DetailHeaderComponent(
                    modifier = modifier,
                    isBookmarked = isBookmarked,
                    header = animeDetails.data.animeDetailsHeaderUiModel,
                    onPictureClick = selectedImage,
                    onClickBookmark = { onClickBookmark(animeDetails.data) }
                )
            }
            item {
                DetailInfoComponent(
                    infoUiModel = animeDetails.data.animeDetailsInfoUiModel,
                    cast = animeCast,
                    staff = animeStaff,
                    onRetryCastAndStaff = onRetryCastAndStaff,
                    onClickShare = onClickShare
                )
            }
        }

        // Empty is also the state before loading starts, so it shows the same placeholder.
        is UiState.Loading, is UiState.Empty -> item { AnimeDetailsShimmer() }

        else -> item {
            ErrorWithRetry(
                modifier = Modifier.fillParentMaxSize(),
                onRetry = onRetryDetails
            )
        }
    }
}

@Composable
internal fun AnimeDetailsShimmer() {
    val roundedCorner8 = RoundedCornerShape(8.dp)
    val shimmer = shimmerBrush()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DetailHeaderComponentShimmer(roundedCorner8, shimmer)
        Spacer(Modifier.height(8.dp))
        DetailInfoComponentShimmer(roundedCorner8, shimmer)
    }
}

@Composable
private fun DetailHeaderComponentShimmer(
    roundedCorner8: RoundedCornerShape,
    shimmer: Brush
) {
    Box(
        modifier = Modifier
            .height(202.dp)
            .fillMaxWidth()
            .background(shimmer)
    )
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(roundedCorner8)
                .background(shimmer)
                .width(124.dp)
                .height(178.dp)
        )
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(roundedCorner8)
                    .background(shimmer)
            )
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(170.dp)
                    .height(18.dp)
                    .clip(roundedCorner8)
                    .background(shimmer)
            )
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .height(18.dp)
                    .clip(roundedCorner8)
                    .background(shimmer)
            )
        }
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .fillMaxWidth()
            .clip(roundedCorner8)
            .background(shimmer),
    )

    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .weight(1F)
                    .height(48.dp)
                    .clip(roundedCorner8)
                    .background(shimmer)
            )
        }
    }

    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(2) {
            Box(
                modifier = Modifier
                    .weight(1F)
                    .height(48.dp)
                    .clip(roundedCorner8)
                    .background(shimmer)
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        ),
    ) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(shimmer)
            )
        }
    }
}

@Composable
private fun DetailInfoComponentShimmer(
    roundedCorner8: RoundedCornerShape,
    shimmer: Brush
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .weight(1F)
                    .height(32.dp)
                    .clip(roundedCorner8)
                    .background(shimmer)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 54.dp)
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(roundedCorner8)
                    .background(shimmer)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(roundedCorner8)
                    .background(shimmer)
            )
        }
    }
}

@Composable
private fun AnimeDetailsComponentPreview(
    animeDetails: UiState<AnimeDetailsUiModel>,
    animeCast: UiState<ImmutableList<AnimeCastUiModel>>,
    animeStaff: UiState<ImmutableList<AnimeStaffUiModel>>
) {
    AniMangaAnimeAndMangaInfoTheme {
        AnimeDetailsComponent(
            isBookmarked = false,
            animeDetails = animeDetails,
            animeCast = animeCast,
            animeStaff = animeStaff,
            headerTitle = {},
            selectedImage = {},
            onRetryDetails = {},
            onRetryCastAndStaff = {},
            onClickBookmark = {},
            onClickShare = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnimeDetailsComponentSuccessPreview() = AnimeDetailsComponentPreview(
    animeDetails = UiState.Success(MediaDetailsPreviewData.anime),
    animeCast = UiState.Success(MediaDetailsPreviewData.animeCast),
    animeStaff = UiState.Success(MediaDetailsPreviewData.animeStaff)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnimeDetailsComponentLoadingPreview() = AnimeDetailsComponentPreview(
    animeDetails = UiState.Loading,
    animeCast = UiState.Loading,
    animeStaff = UiState.Loading
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnimeDetailsComponentErrorPreview() = AnimeDetailsComponentPreview(
    animeDetails = UiState.ErrorException(message = "Something went wrong. Please try again."),
    animeCast = UiState.Loading,
    animeStaff = UiState.Loading
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnimeDetailsComponentCastErrorPreview() {
    val error = UiState.ErrorException(message = "Couldn't load the cast.")
    AnimeDetailsComponentPreview(
        animeDetails = UiState.Success(MediaDetailsPreviewData.anime),
        animeCast = error,
        animeStaff = error
    )
}
