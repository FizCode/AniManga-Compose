package dev.fizcode.mediadetails.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import dev.fizcode.mediadetails.util.DownloadResult
import dev.fizcode.mediadetails.util.ImageDownloader
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.presentation.model.ImageGallery
import dev.fizcode.mediadetails.presentation.model.MediaDetailsEffect
import dev.fizcode.mediadetails.presentation.model.MediaDetailsIntent
import dev.fizcode.mediadetails.presentation.model.MediaDetailsUiState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import dev.fizcode.mediadetails.util.Constant
import dev.fizcode.designsystem.theme.LocalTitleStyle
import dev.fizcode.common.settings.MediaTitles

@Composable
internal fun MediaDetailsScreen(
    mediaId: Int,
    mediaType: String, // decides which details ui is shown: anime or manga
    onBackPressed: () -> Unit,
    onOpenCast: () -> Unit,
    onOpenStaff: () -> Unit,
    onOpenCharacters: () -> Unit
) {
    val mediaDetailsViewModel: MediaDetailsViewModel = koinViewModel(key = "$mediaId") {
        parametersOf(mediaId, mediaType)
    }
    val uiState by mediaDetailsViewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun showMessage(message: String) {
        scope.launch { snackbarHostState.showSnackbar(message = message) }
    }

    LaunchedEffect(mediaDetailsViewModel.effect, lifecycleOwner) {
        mediaDetailsViewModel.effect.collect { effect ->
            showMessage(
                when (effect) {
                    MediaDetailsEffect.Bookmarked -> Constant.BOOKMARKED
                    MediaDetailsEffect.Unbookmarked -> Constant.UNBOOKMARKED
                }
            )
        }
    }

    // Opens the share sheet with the shared text and a link that opens this media in the app.
    val onShare: (title: String, content: String) -> Unit = { title, content ->
        val text = buildShareText(
            title = title,
            content = content,
            mediaType = mediaType,
            mediaId = mediaId
        )
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            if (title.isNotBlank()) putExtra(Intent.EXTRA_SUBJECT, title)
        }
        context.startActivity(Intent.createChooser(sendIntent, Constant.SHARE_CHOOSER_TITLE))
    }

    fun download(imageUrl: String) {
        scope.launch {
            // The progress message is dismissed as soon as the result is known.
            val progress = launch { snackbarHostState.showSnackbar(message = Constant.DOWNLOAD_STARTED) }
            val result = ImageDownloader.download(context, imageUrl)
            progress.cancel()
            snackbarHostState.showSnackbar(
                message = when (result) {
                    DownloadResult.Saved -> Constant.DOWNLOAD_SAVED
                    DownloadResult.Failed -> Constant.DOWNLOAD_FAILED
                    DownloadResult.StillRunning -> Constant.DOWNLOAD_STILL_RUNNING
                }
            )
        }
    }
    var pendingDownload by remember { mutableStateOf<String?>(null) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val url = pendingDownload
        pendingDownload = null
        if (granted && url != null) download(url) else showMessage(Constant.DOWNLOAD_PERMISSION_DENIED)
    }
    val onDownloadImage: (String) -> Unit = { imageUrl ->
        val hasPermission = !ImageDownloader.needsStoragePermission ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            download(imageUrl)
        } else {
            pendingDownload = imageUrl
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    MediaDetailsContent(
        mediaKind = remember(mediaType) { MediaKind.from(mediaType) },
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = mediaDetailsViewModel::onIntent,
        onBackPressed = onBackPressed,
        onOpenCast = onOpenCast,
        onOpenStaff = onOpenStaff,
        onOpenCharacters = onOpenCharacters,
        onShare = onShare,
        onDownloadImage = onDownloadImage
    )
}

/** Stateless content of the screen, it only takes state and callbacks so it can be previewed. */
@Composable
private fun MediaDetailsContent(
    mediaKind: MediaKind,
    uiState: MediaDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (MediaDetailsIntent) -> Unit,
    onBackPressed: () -> Unit,
    onOpenCast: () -> Unit,
    onOpenStaff: () -> Unit,
    onOpenCharacters: () -> Unit,
    onShare: (title: String, content: String) -> Unit,
    onDownloadImage: (String) -> Unit
) {
    // The title bar and the share text show the title in the style the user picked.
    val titleStyle = LocalTitleStyle.current
    var headerTitles = MediaTitles("")
    val headerTitle = { headerTitles.resolve(titleStyle) }
    var selectedImage by remember { mutableStateOf<ImageGallery?>(null) }
    val onClickShare: (String) -> Unit = { content -> onShare(headerTitle(), content) }

    MediaDetailsLayout(
        headerTitle = headerTitle,
        snackbarHostState = snackbarHostState,
        selectedImage = selectedImage,
        onDismissImage = { selectedImage = null },
        onBackPressed = onBackPressed,
        onDownloadImage = onDownloadImage
    ) { observerModifier ->
        when (mediaKind) {
            MediaKind.ANIME -> {
                AnimeDetailsComponent(
                    modifier = observerModifier,
                    isBookmarked = uiState.isBookmarked,
                    animeDetails = uiState.animeDetails,
                    animeCast = uiState.animeCast,
                    animeStaff = uiState.animeStaff,
                    headerTitle = { headerTitles = it },
                    selectedImage = { selectedImage = it },
                    onRetryDetails = { onIntent(MediaDetailsIntent.RetryDetails) },
                    onRetryCastAndStaff = { onIntent(MediaDetailsIntent.RetryCastAndStaff) },
                    onMoreCastClick = onOpenCast,
                    onMoreStaffClick = onOpenStaff,
                    onClickShare = onClickShare,
                    onClickBookmark = { onIntent(MediaDetailsIntent.ToggleBookmark) }
                )
            }

            MediaKind.MANGA -> {
                MangaDetailsComponent(
                    modifier = observerModifier,
                    isBookmarked = uiState.isBookmarked,
                    mangaDetails = uiState.mangaDetails,
                    characters = uiState.mangaCharacters,
                    headerTitle = { headerTitles = it },
                    selectedImage = { selectedImage = it },
                    onRetryDetails = { onIntent(MediaDetailsIntent.RetryMangaDetails) },
                    onRetryCharacters = { onIntent(MediaDetailsIntent.RetryMangaCharacters) },
                    onMoreCharactersClick = onOpenCharacters,
                    onClickShare = onClickShare,
                    onClickBookmark = { onIntent(MediaDetailsIntent.ToggleBookmark) }
                )
            }
        }
    }
}

/** Stateless frame of the screen (top bar, back button, image popup); the media body is slotted in. */
@Composable
private fun MediaDetailsLayout(
    headerTitle: () -> String,
    snackbarHostState: SnackbarHostState,
    selectedImage: ImageGallery?,
    onDismissImage: () -> Unit,
    onBackPressed: () -> Unit,
    onDownloadImage: (String) -> Unit = {},
    content: @Composable (observerModifier: Modifier) -> Unit
) {
    val slideInAnimation = slideInVertically(initialOffsetY = { -it / 2 })
    val showTopBars = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val observerModifier = Modifier.onGloballyPositioned { coordinates ->
        val yPosition = coordinates.positionInWindow().y
        showTopBars.value = yPosition < with(density) { 200.dp.toPx() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AnimatedVisibility(
                visible = showTopBars.value,
                enter = slideInAnimation + fadeIn(),
                exit = fadeOut()
            ) {
                TitleBar(
                    headerTitle = headerTitle(),
                    onBackPressed = onBackPressed
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        // Inner padding not used to avoid bumping bug.
        innerPadding.calculateTopPadding()
        content(observerModifier)
    }
    if (!showTopBars.value) {
        FloatingBackButton(onBackPressed = onBackPressed)
    }
    selectedImage?.let { gallery ->
        ImagesBottomSheet(
            gallery = gallery,
            onDownload = onDownloadImage,
            onDismiss = onDismissImage
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleBar(
    headerTitle: String,
    onBackPressed: () -> Unit
) = TopAppBar(
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

@Composable
private fun FloatingBackButton(
    onBackPressed: () -> Unit
) = IconButton(
    modifier = Modifier
        .padding(top = 32.dp)
        .padding(horizontal = 4.dp)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnimeDetailsScreenPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MediaDetailsLayout(
            headerTitle = { MediaDetailsPreviewData.anime.animeDetailsHeaderUiModel.title },
            snackbarHostState = remember { SnackbarHostState() },
            selectedImage = null,
            onDismissImage = {},
            onBackPressed = {}
        ) { observerModifier ->
            AnimeDetailsComponent(
                modifier = observerModifier,
                isBookmarked = false,
                animeDetails = UiState.Success(MediaDetailsPreviewData.anime),
                animeCast = UiState.Success(MediaDetailsPreviewData.animeCast),
                animeStaff = UiState.Success(MediaDetailsPreviewData.animeStaff),
                headerTitle = {},
                selectedImage = {},
                onRetryDetails = {},
                onRetryCastAndStaff = {},
                onMoreCastClick = {},
                onMoreStaffClick = {},
                onClickBookmark = {},
                onClickShare = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MangaDetailsScreenPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MediaDetailsLayout(
            headerTitle = { MediaDetailsPreviewData.manga.mangaDetailsHeaderUiModel.title },
            snackbarHostState = remember { SnackbarHostState() },
            selectedImage = null,
            onDismissImage = {},
            onBackPressed = {}
        ) { observerModifier ->
            MangaDetailsComponent(
                modifier = observerModifier,
                isBookmarked = false,
                mangaDetails = UiState.Success(MediaDetailsPreviewData.manga),
                characters = UiState.Success(MediaDetailsPreviewData.mangaCharacters),
                headerTitle = {},
                selectedImage = {},
                onRetryDetails = {},
                onRetryCharacters = {},
                onMoreCharactersClick = {},
                onClickBookmark = {},
                onClickShare = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnimeDetailsScreenLoadingPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MediaDetailsLayout(
            headerTitle = { "" },
            snackbarHostState = remember { SnackbarHostState() },
            selectedImage = null,
            onDismissImage = {},
            onBackPressed = {}
        ) { observerModifier ->
            AnimeDetailsComponent(
                modifier = observerModifier,
                isBookmarked = false,
                animeDetails = UiState.Loading,
                animeCast = UiState.Loading,
                animeStaff = UiState.Loading,
                headerTitle = {},
                selectedImage = {},
                onRetryDetails = {},
                onRetryCastAndStaff = {},
                onMoreCastClick = {},
                onMoreStaffClick = {},
                onClickBookmark = {},
                onClickShare = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MangaDetailsScreenLoadingPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MediaDetailsLayout(
            headerTitle = { "" },
            snackbarHostState = remember { SnackbarHostState() },
            selectedImage = null,
            onDismissImage = {},
            onBackPressed = {}
        ) { observerModifier ->
            MangaDetailsComponent(
                modifier = observerModifier,
                isBookmarked = false,
                mangaDetails = UiState.Loading,
                characters = UiState.Loading,
                headerTitle = {},
                selectedImage = {},
                onRetryDetails = {},
                onRetryCharacters = {},
                onMoreCharactersClick = {},
                onClickBookmark = {},
                onClickShare = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MangaDetailsContentPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MediaDetailsContent(
            mediaKind = MediaKind.MANGA,
            uiState = MediaDetailsUiState(
                mangaDetails = UiState.Success(MediaDetailsPreviewData.manga),
                mangaCharacters = UiState.Success(MediaDetailsPreviewData.mangaCharacters)
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {},
            onBackPressed = {},
            onOpenCast = {},
            onOpenStaff = {},
            onOpenCharacters = {},
            onShare = { _, _ -> },
            onDownloadImage = {}
        )
    }
}
