package dev.fizcode.mediadetailinfo.model

import androidx.compose.ui.graphics.vector.ImageVector
import dev.fizcode.designsystem.icon.CustomIcon

@Deprecated(message = "DELETE AFTER FINISHED TESTING!")
data class InformationTableUiModel(
    val title: String,
    val desc: String,
    val type: String,
    val snsIcon: ImageVector = CustomIcon.FILL_LINK,
    val link: String = ""
)
