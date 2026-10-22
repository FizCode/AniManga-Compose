package dev.fizcode.mediadetails.presentation.info.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.other.UnavailableNotice
import dev.fizcode.designsystem.util.base.shimmer
import dev.fizcode.mediadetails.presentation.info.model.MangaCharacterUiModel
import dev.fizcode.mediadetails.presentation.info.util.Constant
import dev.fizcode.mediadetails.presentation.isError
import dev.fizcode.mediadetails.util.Constant as MediaDetailsConstant
import kotlinx.collections.immutable.ImmutableList
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.presentation.MediaDetailsPreviewData

@Composable
internal fun CharactersTabComponent(
    characters: UiState<ImmutableList<MangaCharacterUiModel>>,
    onRetry: () -> Unit,
    onMoreClick: () -> Unit,
    onCharacterClick: () -> Unit
) = Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
    when {
        characters is UiState.Loading -> SectionContainerShimmer {
            repeat(5) { StaffShimmer() }
        }

        characters is UiState.Success -> SectionContainer(
            title = Constant.CHARACTERS,
            onMoreClick = onMoreClick
        ) {
            characters.data.forEach { character ->
                CharacterRow(character = character, onClick = onCharacterClick)
            }
        }

        characters.isError() -> UnavailableNotice(
            message = MediaDetailsConstant.CHARACTERS_UNAVAILABLE,
            onRetry = onRetry
        )
    }
    Spacer(Modifier.height(4.dp))
}

@Composable
internal fun CharacterRow(
    character: MangaCharacterUiModel,
    onClick: () -> Unit
) = Row(
    modifier = Modifier.clickable { onClick() },
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    AsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .shimmer()
            .width(50.dp)
            .height(72.dp),
        contentScale = ContentScale.Crop,
        contentDescription = Constant.CHARACTER_IMAGE,
        model = character.image
    )
    Column(modifier = Modifier.weight(1F)) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            text = character.name
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Start,
            text = character.role
        )
    }
}

@Composable
private fun CharactersTabPreviewContent(
    characters: UiState<ImmutableList<MangaCharacterUiModel>>
) {
    AniMangaAnimeAndMangaInfoTheme {
        CharactersTabComponent(
            characters = characters,
            onRetry = {},
            onMoreClick = {},
            onCharacterClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CharactersTabPreview() = CharactersTabPreviewContent(
    UiState.Success(MediaDetailsPreviewData.mangaCharacters)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CharactersTabLoadingPreview() = CharactersTabPreviewContent(UiState.Loading)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CharactersTabErrorPreview() = CharactersTabPreviewContent(
    UiState.Error(message = "Something went wrong. Please try again.")
)
