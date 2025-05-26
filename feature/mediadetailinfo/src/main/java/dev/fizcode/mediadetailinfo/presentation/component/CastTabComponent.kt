package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetailinfo.model.AnimeCast
import dev.fizcode.mediadetailinfo.model.AnimeCharacters
import dev.fizcode.mediadetailinfo.model.AnimeStaff
import dev.fizcode.mediadetailinfo.util.Constant

@Composable
internal fun CastTabComponent(
    cast: AnimeCast
) = Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
    SectionContainer(
        title = Constant.CHARACTERS_AND_VA,
        onMoreClick = { /* TODO */ }
    ) {
        cast.characters.forEach { data ->
            CharacterVA(characterData = data)
        }
    }

    SectionContainer(
        title = Constant.STAFF,
        onMoreClick = { /* TODO */ }
    ) {
        cast.animeStaffs.forEach { data ->
            Staffs(staffData = data)
        }
    }
}

@Composable
private fun SectionContainer(
    title: String,
    onMoreClick: (() -> Unit),
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
private fun CharacterVA(
    characterData: AnimeCharacters
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
        contentDescription = Constant.CHARACTER_IMAGE,
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
            text = characterData.character
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
            text = characterData.characterRole
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
        contentDescription = Constant.VA_IMAGE,
        model = characterData.voiceActorImage
    )
}

@Composable
private fun Staffs(
    staffData: AnimeStaff
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
        contentDescription = Constant.STAFF_IMAGE,
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
private fun CastTabPreview() {

    val characterVADummy = listOf(
        AnimeCharacters(
            characterImage = "",
            character = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceActorLang = "Japanese"
        ),
        AnimeCharacters(
            characterImage = "",
            character = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceActorLang = "Japanese"
        ),
        AnimeCharacters(
            characterImage = "",
            character = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceActorLang = "Japanese"
        ),
        AnimeCharacters(
            characterImage = "",
            character = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceActorLang = "Japanese"
        ),
        AnimeCharacters(
            characterImage = "",
            character = "John Doe",
            characterRole = "Main",
            voiceActorImage = "",
            voiceActorName = "John Done",
            voiceActorLang = "Japanese"
        ),
    )

    val staffsDummy = listOf(
        AnimeStaff(
            staffId = 0,
            image = "",
            name = "John Doe",
            role = "Director"
        ),
        AnimeStaff(
            staffId = 0,
            image = "",
            name = "John Doe",
            role = "Director"
        ),
        AnimeStaff(
            staffId = 0,
            image = "",
            name = "John Doe",
            role = "Director"
        )
    )

    CastTabComponent(
        AnimeCast(
            characters = characterVADummy.take(1),
            animeStaffs = staffsDummy
        )
    )
}
