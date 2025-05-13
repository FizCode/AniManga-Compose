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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.mediadetailheader.presentation.DetailHeaderComponent
import dev.fizcode.mediadetailinfo.presentation.DetailInfoComponent

@Composable
internal fun MediaDetailsScreen(
    mediaId: Int,
    mediaType: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DetailHeaderComponent()
        DetailInfoComponent()
        Text("mediaId: $mediaId\nmediaType: $mediaType")
        Spacer(Modifier.height(16.dp))
    }
    IconButton(
        modifier = Modifier
            .padding(top = 32.dp, start = 4.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = {}
    ) {
        Icon(
            imageVector = CustomIcon.FILL_ARROW_BACK,
            contentDescription = "Back Button"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MediaDetailScreenPreview() {
    MediaDetailsScreen(mediaId = 1, mediaType = "Lorem")
}
