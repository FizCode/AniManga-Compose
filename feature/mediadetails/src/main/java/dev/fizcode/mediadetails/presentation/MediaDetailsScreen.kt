package dev.fizcode.mediadetails.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.mediadetailheader.presentation.DetailHeaderComponent
import dev.fizcode.mediadetailinfo.presentation.DetailInfoComponent
import dev.fizcode.mediadetails.util.Constant

@Composable
internal fun MediaDetailsScreen(
    mediaId: Int,
    mediaType: String, // to determine what's details ui should be ex: Anime, Manga, VA, etc.
    onBackPressed: () -> Unit,
    mediaDetailsViewModel: MediaDetailsViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        mediaDetailsViewModel.fetchMediaId(mediaId = mediaId)
    }

    val animeDetails by mediaDetailsViewModel.animeDetails.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        when (val state = animeDetails) {
            is UiState.Success -> {
                DetailHeaderComponent(header = state.data.animeDetailsHeaderUiModel)
                DetailInfoComponent(
                    info = state.data.animeDetailsInfoUiModel,
                    onClickShare = {}
                )
            }

            else -> {}
        }
        Spacer(Modifier.height(16.dp))
    }
    IconButton(
        modifier = Modifier
            .padding(top = 32.dp, start = 4.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = onBackPressed
    ) {
        Icon(
            imageVector = CustomIcon.FILL_ARROW_BACK,
            contentDescription = Constant.BACK_BUTTON
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MediaDetailScreenPreview() {
    MediaDetailsScreen(mediaId = 1, mediaType = "Lorem", onBackPressed = {})
}
