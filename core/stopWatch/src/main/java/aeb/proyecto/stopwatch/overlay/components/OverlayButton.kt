package aeb.proyecto.stopwatch.overlay.components

import aeb.proyecto.stopwatch.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

/**
 * Baseline interactive typographic button blueprint serving as the foundational building block
 * for all window overlay action controls.
 * Enforces uniform padding standards and strict single-line text truncation to safe-keep visual
 * geometry against layout distortion in confined desktop overlay spaces.
 *
 * @param modifier The structural composition modifier layout layout adjustment token.
 * @param text The string text label resource rendered within the button canvas.
 * @param color The typographic color asset targeted to paint the label text content.
 * @param onClick The functional executable closure token invoked upon receiving user touch interactions.
 */
@Composable
fun OverlayButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = spacing4, vertical = spacing8)
    ) {
        LabelLargeText(
            modifier = modifier,
            text = text,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Specialized destructive action variant signaling systemic tracking cancellation procedures.
 * Hardcodes a high-visibility alert red color palette profile ([Color(0xFFED4337)]) to communicate
 * irreversible state resets semantically.
 *
 * @param modifier The structural composition modifier layout layout adjustment token.
 * @param text Localized localized text string defaulted to the framework service cancellation asset.
 * @param color The typographic paint color defaulted to warning alert red.
 * @param onClick The functional executable closure token driving active session teardowns.
 */
@Composable
fun CancelButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.service_cancel),
    color: Color = Color(0xFFED4337),
    onClick: () -> Unit
){
    OverlayButton(
        modifier = modifier,
        text = text,
        color = color,
        onClick = onClick
    )
}

/**
 * Specialized functional variant signaling temporal runtime suspensions on active tracking loops.
 *
 * @param modifier The structural composition modifier layout layout adjustment token.
 * @param text Localized localized text string defaulted to the framework service pause/stop asset.
 * @param color The thematic contextual paint color injected dynamically from parent components.
 * @param onClick The functional executable closure token halting operational countdown tickers.
 */
@Composable
fun PauseButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.service_stop),
    color: Color,
    onClick: () -> Unit
){
    OverlayButton(
        modifier = modifier,
        text = text,
        color = color,
        onClick = onClick
    )
}

/**
 * Specialized functional variant triggering re-activation procedures over suspended tracking states.
 *
 * @param modifier The structural composition modifier layout layout adjustment token.
 * @param text Localized localized text string defaulted to the framework service resume asset.
 * @param color The thematic contextual paint color injected dynamically from parent components.
 * @param onClick The functional executable closure token re-igniting core computation threads.
 */
@Composable
fun ResumeButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.service_resume),
    color: Color,
    onClick: () -> Unit
){
    OverlayButton(
        modifier = modifier,
        text = text,
        color = color,
        onClick = onClick
    )
}

/**
 * Specialized terminal variant signaling successful execution target accomplishments.
 *
 * @param text Localized localized text string defaulted to the framework service completion asset.
 * @param color The thematic contextual paint color injected dynamically from parent components.
 * @param onClick The functional executable closure token finishing tracking routines cleanly.
 */
@Composable
fun FinishButton(
    text: String = stringResource(R.string.service_finish),
    color: Color,
    onClick: () -> Unit
){
    OverlayButton(
        text = text,
        color = color,
        onClick = onClick
    )
}