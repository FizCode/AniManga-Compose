package dev.fizcode.designsystem.component.other

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import dev.fizcode.designsystem.util.Constant.Component

@Composable
fun HyperlinkText(
    text: String,
    url: String,
    modifier: Modifier = Modifier,
    linkTextColor: Color = MaterialTheme.colorScheme.primary,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.None,
    fontSize: TextUnit = TextUnit.Unspecified,
    color: Color = MaterialTheme.colorScheme.onBackground,
    style: TextStyle = TextStyle.Default.copy(textAlign = TextAlign.Center)
) {
    val annotatedText = buildAnnotatedString {
        pushStringAnnotation(tag = Component.URL, annotation = url)
        withStyle(
            SpanStyle(
                color = linkTextColor,
                fontSize = fontSize,
                fontWeight = linkTextFontWeight,
                textDecoration = linkTextDecoration
            )
        ) {
            append(text)
        }
        pop()
    }

    val uriHandler = LocalUriHandler.current

    Text(
        modifier = modifier.clickable {
            annotatedText.getStringAnnotations(tag = Component.URL, start = 0, end = text.length)
                .firstOrNull()?.let {
                    uriHandler.openUri(it.item)
                }
        },
        style = style,
        color = color,
        fontSize = fontSize,
        text = annotatedText
    )
}


@Composable
fun MultipleHyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    hyperlinks: List<String>,
    linkTextColor: Color = MaterialTheme.colorScheme.primary,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.None,
    fontSize: TextUnit = TextUnit.Unspecified,
    color: Color = MaterialTheme.colorScheme.onBackground,
    style: TextStyle = TextStyle.Default.copy(textAlign = TextAlign.Center)
) {
    val annotatedString = remember(fullText, linkText, hyperlinks) {
        buildAnnotatedString {
            var currentIndex = 0
            linkText.forEachIndexed { index, word ->
                val start = fullText.indexOf(word, currentIndex)
                val end = start + word.length

                // Append the non-link part before the current link
                if (start > currentIndex) {
                    append(fullText.substring(currentIndex, start))
                }

                // Append the link using LinkAnnotation
                withLink(LinkAnnotation.Url(hyperlinks[index])) {
                    withStyle(
                        SpanStyle(
                            color = linkTextColor,
                            fontSize = fontSize,
                            fontWeight = linkTextFontWeight,
                            textDecoration = linkTextDecoration
                        )
                    ) {
                        append(word)
                    }
                }

                currentIndex = end
            }

            // Append the rest of the text after the last link
            if (currentIndex < fullText.length) {
                append(fullText.substring(currentIndex))
            }
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        fontSize = fontSize,
        color = color,
        style = style
    )
}

