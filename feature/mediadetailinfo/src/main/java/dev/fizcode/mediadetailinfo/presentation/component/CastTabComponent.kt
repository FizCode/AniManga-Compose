package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetailinfo.model.AnimeCastUiModel
import dev.fizcode.mediadetailinfo.model.AnimeStaffUiModel
import dev.fizcode.mediadetailinfo.util.Constant
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun CastTabComponent(
    castUiModel: UiState<ImmutableList<AnimeCastUiModel>>,
    staffUiModel: UiState<ImmutableList<AnimeStaffUiModel>>,
    onMoreClick: () -> Unit,
    onCastImageClick: () -> Unit
) = Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
    when (castUiModel) {
        is UiState.Loading -> {
            SectionContainerShimmer {
                repeat(5) {
                    CastShimmer()
                }
            }
        }

        is UiState.Success -> {
            SectionContainer(
                title = Constant.CHARACTERS_AND_VA,
                onMoreClick = onMoreClick
            ) {
                castUiModel.data.forEach { data ->
                    CharacterCast(
                        characterData = data,
                        onCastImageClick = onCastImageClick
                    )
                }
            }
        }

        else -> {}
    }

    when (staffUiModel) {
        is UiState.Loading -> {
            SectionContainerShimmer {
                repeat(5) {
                    StaffShimmer()
                }
            }
        }

        is UiState.Success -> {
            SectionContainer(
                title = Constant.STAFF,
                onMoreClick = onMoreClick
            ) {
                staffUiModel.data.forEach { data ->
                    Staffs(
                        staffData = data,
                        onCastImageClick = onCastImageClick
                    )
                }
            }
        }

        else -> {

        }
    }
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun SectionContainer(
    title: String,
    onMoreClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .clickable { onMoreClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(24.dp))
        Text(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = title
        )
        Icon(
            tint = MaterialTheme.colorScheme.onBackground,
            imageVector = CustomIcon.OUTL_ARROW_RIGHT,
            contentDescription = Constant.MORE_ICON
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Composable
private fun SectionContainerShimmer(
    content: @Composable ColumnScope.() -> Unit
) {
    val shimmer = shimmerBrush()
    Column(Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(shimmer)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CastTabPreview() {

    CastTabComponent(
        castUiModel = UiState.Loading,
        staffUiModel = UiState.Loading,
        onMoreClick = {},
        onCastImageClick = {}
    )
}
