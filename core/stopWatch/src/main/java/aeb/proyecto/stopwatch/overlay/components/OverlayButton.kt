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