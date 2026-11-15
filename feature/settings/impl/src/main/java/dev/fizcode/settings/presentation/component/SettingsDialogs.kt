package dev.fizcode.settings.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle
import dev.fizcode.designsystem.theme.AniMangaTheme
import dev.fizcode.settings.presentation.model.PendingFilterChange
import dev.fizcode.settings.presentation.model.icon
import dev.fizcode.settings.presentation.model.label
import dev.fizcode.settings.util.Constant

/** Lists every [ThemeMode] with its icon, picking one applies it right away. */
@Composable
internal fun ThemeDialog(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) = AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = Constant.THEME_TITLE) },
    text = {
        Column(modifier = Modifier.selectableGroup()) {
            ThemeMode.entries.forEach { mode ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = mode == selected,
                            role = Role.RadioButton,
                            onClick = { onSelect(mode) }
                        )
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = mode.icon,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                        text = mode.label
                    )
                    RadioButton(selected = mode == selected, onClick = null)
                }
            }
        }
    },
    confirmButton = {
        TextButton(onClick = onDismiss) { Text(text = Constant.CLOSE) }
    }
)

/**
 * Asks the user to confirm a [change] and explains what the new choice does. When the change has a
 * confirmation phrase the user has to type it before the Change button turns on.
 */
@Composable
internal fun ConfirmFilterDialog(
    change: PendingFilterChange,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var input by rememberSaveable { mutableStateOf("") }
    val confirmed = change.isConfirmed(input)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = change.dialogTitle) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (change.confirmationPhrase == null) {
                    Text(text = Constant.confirmChange(change.label))
                }
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = change.description
                )
                change.warning?.let { warning ->
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        text = buildAnnotatedString {
                            append(warning)
                            change.confirmationPhrase?.let { phrase ->
                                append(" ${Constant.TYPE_PREFIX} ")
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.error,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) { append(phrase) }
                                append(" ${Constant.TYPE_SUFFIX}")
                            }
                        }
                    )
                }
                if (change.confirmationPhrase != null) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = input,
                        onValueChange = { input = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { if (confirmed) onConfirm() })
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = confirmed) { Text(text = Constant.CHANGE) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = Constant.CANCEL) }
        }
    )
}

@Composable
internal fun AboutDialog(
    version: String,
    onDismiss: () -> Unit
) = AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = Constant.ABOUT_TITLE) },
    text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "${Constant.APP_NAME} v$version"
            )
            Text(text = Constant.ABOUT_DESCRIPTION)
            Text(
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                text = Constant.copyright()
            )
        }
    },
    confirmButton = {
        TextButton(onClick = onDismiss) { Text(text = Constant.CLOSE) }
    }
)

@Preview(showBackground = true)
@Composable
private fun ThemeDialogPreview() {
    AniMangaTheme {
        ThemeDialog(selected = ThemeMode.DEFAULT, onSelect = {}, onDismiss = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmFilterDialogPreview() {
    AniMangaTheme {
        ConfirmFilterDialog(
            change = PendingFilterChange.Titling(TitleStyle.SYNONYM),
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmGreyDialogPreview() {
    AniMangaTheme {
        ConfirmFilterDialog(
            change = PendingFilterChange.Nsfw(NsfwFilter.GREY),
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmNotSafeDialogPreview() {
    AniMangaTheme {
        ConfirmFilterDialog(
            change = PendingFilterChange.Nsfw(NsfwFilter.NOT_SAFE),
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutDialogPreview() {
    AniMangaTheme {
        AboutDialog(version = "0.1.0-Alpha", onDismiss = {})
    }
}
