package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetailinfo.model.CharacterVAUiModel
import dev.fizcode.mediadetailinfo.model.StaffsUiModel

@Composable
internal fun StaffTabComponent() {

    val characterVADummy = listOf(
        CharacterVAUiModel(
            characterImage = "",
            characterName = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceLanguage = "Japanese"
        ),
        CharacterVAUiModel(
            characterImage = "",
            characterName = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceLanguage = "Japanese"
        ),
        CharacterVAUiModel(
            characterImage = "",
            characterName = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceLanguage = "Japanese"
        ),
        CharacterVAUiModel(
            characterImage = "",
            characterName = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceLanguage = "Japanese"
        ),
        CharacterVAUiModel(
            characterImage = "",
            characterName = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceLanguage = "Japanese"
        )
    )

    val staffsDummy = listOf(
        StaffsUiModel(
            image = "",
            name = "John Doe",
            role = "Director"
        ),
        StaffsUiModel(
            image = "",
            name = "John Doe",
            role = "Director"
        ),
        StaffsUiModel(
            image = "",
            name = "John Doe",
            role = "Director"
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CharacterVASection(
            characterData = characterVADummy
        )
        StaffsSection(staffData = staffsDummy)
    }
}

@Composable
private fun CharacterVASection(
    characterData: List<CharacterVAUiModel>
) = Column(
    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
) {
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(start = 24.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = "Characters & Voice Actors"
        )
        Icon(
            modifier = Modifier.clickable(onClick = {}),
            tint = MaterialTheme.colorScheme.onBackground,
            imageVector = CustomIcon.OUTL_ARROW_RIGHT,
            contentDescription = "More"

        )
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        characterData.forEach { data ->
            CharacterVA(characterData = data)
        }
    }
}

@Composable
private fun StaffsSection(
    staffData: List<StaffsUiModel>
) = Column(
    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(start = 24.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = "Staff"
        )
        Icon(
            modifier = Modifier.clickable(onClick = {}),
            tint = MaterialTheme.colorScheme.onBackground,
            imageVector = CustomIcon.OUTL_ARROW_RIGHT,
            contentDescription = "More"

        )
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        staffData.forEach { data ->
            Staffs(staffData = data)
        }
    }
}

@Composable
private fun CharacterVA(
    characterData: CharacterVAUiModel
) = Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    AsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(shimmerBrush(targetValue = 1300f))
            .width(50.dp)
            .height(72.dp),
        contentScale = ContentScale.Crop,
        contentDescription = "",
        model = characterData.characterImage
    )
    Column(
        modifier = Modifier.weight(1F)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            text = characterData.characterName
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Start,
            text = characterData.characterRole
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.End,
            text = characterData.voiceLanguage
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.End,
            text = characterData.voiceActorName
        )
    }
    AsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(shimmerBrush(targetValue = 1300f))
            .width(50.dp)
            .height(72.dp),
        contentScale = ContentScale.Crop,
        contentDescription = "",
        model = characterData.voiceActorImage
    )
}

@Composable
private fun Staffs(
    staffData: StaffsUiModel
) = Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    AsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(shimmerBrush(targetValue = 1300f))
            .width(50.dp)
            .height(72.dp),
        contentScale = ContentScale.Crop,
        contentDescription = "",
        model = staffData.image
    )
    Column(
        modifier = Modifier.weight(1F)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            text = staffData.name
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Start,
            text = staffData.role
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StaffTabPreview() {
    StaffTabComponent()
}
