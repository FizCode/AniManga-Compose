package dev.fizcode.mediadetails.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.mediadetails.util.Constant

@OptIn(ExperimentalMaterial3Api::class)
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
    val animeCast by mediaDetailsViewModel.animeCast.collectAsStateWithLifecycle()
    val animeStaff by mediaDetailsViewModel.animeStaff.collectAsStateWithLifecycle()

    var headerTitle = ""
    var selectedImage by remember { mutableStateOf<String?>(null) }

    val slideInAnimation = slideInVertically(initialOffsetY = { -it / 2 })
    val showTopBars = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val observerModifier = Modifier.onGloballyPositioned { coordinates ->
        val yPosition = coordinates.positionInWindow().y
        showTopBars.value = yPosition < with(density) { 200.dp.toPx() }
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showTopBars.value,
                enter = slideInAnimation + fadeIn(),
                exit = fadeOut()
            ) {
                TopAppBar(
                    title = {
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = headerTitle,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = CustomIcon.FILL_ARROW_BACK,
                                contentDescription = Constant.BACK_BUTTON
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    )
                )
            }
        }
    ) { innerPadding ->
        // We don't use inner padding to set content edges padding
        innerPadding.calculateTopPadding()
        Box(modifier = Modifier.fillMaxSize()) {
            AnimeDetailsComponent(
                modifier = observerModifier,
                animeDetails = animeDetails,
                animeCast = animeCast,
                animeStaff = animeStaff,
                headerTitle = { headerTitle = it },
                selectedImage = { selectedImage = it }
            )
            if (!showTopBars.value) {
                IconButton(
                    modifier = Modifier
                        .padding(top = 32.dp, start = 4.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    onClick = onBackPressed
                ) {
                    Icon(
                        imageVector = CustomIcon.FILL_ARROW_BACK,
                        contentDescription = Constant.BACK_BUTTON
                    )
                }
            }
            selectedImage?.let { imageUrl ->
                PopUpImage(imageUrl = imageUrl, onDismiss = { selectedImage = null })
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MediaDetailScreenPreview() {
    MediaDetailsScreen(mediaId = 1, mediaType = "Lorem", onBackPressed = {})
}
