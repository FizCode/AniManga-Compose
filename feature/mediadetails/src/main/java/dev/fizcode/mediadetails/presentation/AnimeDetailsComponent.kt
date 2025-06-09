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
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetailheader.presentation.DetailHeaderComponent
import dev.fizcode.mediadetailinfo.model.AnimeCastUiModel
import dev.fizcode.mediadetailinfo.model.AnimeStaffUiModel
import dev.fizcode.mediadetailinfo.presentation.DetailInfoComponent
import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun AnimeDetailsComponent(
    modifier: Modifier = Modifier,
    animeDetails: UiState<AnimeDetailsUiModel>,
    animeCast: UiState<ImmutableList<AnimeCastUiModel>>,
    animeStaff: UiState<ImmutableList<AnimeStaffUiModel>>,
    headerTitle: (String) -> Unit,
    selectedImage: (String) -> Unit
) = LazyColumn(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    when (animeDetails) {
        is UiState.Success -> {
            headerTitle(animeDetails.data.animeDetailsHeaderUiModel.title)
            item {
                DetailHeaderComponent(
                    modifier = modifier,
                    header = animeDetails.data.animeDetailsHeaderUiModel,
                    onPictureClick = selectedImage
                )
            }
            item {
                DetailInfoComponent(
                    infoUiModel = animeDetails.data.animeDetailsInfoUiModel,
                    cast = animeCast,
                    staff = animeStaff,
                    onClickShare = {}
                )
            }
        }

        is UiState.Loading -> item { AnimeDetailsShimmer() }

        else -> {}
    }
}

@Composable
private fun AnimeDetailsShimmer() {
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
@Preview(showBackground = true)
private fun AnimeDetailsComponentPreview() {
    AnimeDetailsShimmer()
}
