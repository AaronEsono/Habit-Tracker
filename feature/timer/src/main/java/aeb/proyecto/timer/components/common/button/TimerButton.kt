package aeb.proyecto.timer.components.common.button

import aeb.proyecto.timer.R
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A highly configurable button component for timer controls.
 * Designed for consistent styling across the timer interface while allowing
 * dynamic color and icon injection.
 *
 * @param modifier Applied to the [ElevatedButton].
 * @param iconButton The [ImageVector] displayed at the start of the button.
 * @param title The text label displayed next to the icon.
 * @param colorButton The background color of the button.
 * @param contentColorButton The color applied to both the icon and the text.
 * @param onClick The action triggered upon clicking the button.
 */
@Composable
fun TimerButton(
    modifier: Modifier = Modifier,
    iconButton:ImageVector,
    title: String,
    colorButton: Color,
    contentColorButton:Color,
    onClick: () -> Unit,
){
    CustomRipple {
        ElevatedButton(
            modifier = modifier.width(160.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorButton
            ),
            shape = RoundedCornerShape(spacing12)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = spacing4)
            ){

                Icon(
                    iconButton,
                    contentDescription = "icon timer button",
                    tint = contentColorButton
                )

                LabelLargeText(
                    text = title,
                    color = contentColorButton,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = spacing6),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
    }
}

/**
 * Action button to resume a paused timer.
 */
@Composable
fun ResumeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    TimerButton(
        modifier = modifier,
        iconButton = Icons.Filled.PlayArrow,
        title = stringResource(R.string.timer_button_resume),
        colorButton = MaterialTheme.colorScheme.surfaceContainerLowest,
        contentColorButton = getContrastColor(MaterialTheme.colorScheme.surfaceContainerLowest)
    ){ onClick() }
}

/**
 * Action button to cancel the current timer session.
 */
@Composable
fun CancelButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    TimerButton(
        modifier = modifier,
        iconButton = Icons.Filled.Close,
        title = stringResource(R.string.timer_button_cancel),
        colorButton = MaterialTheme.colorScheme.onBackground,
        contentColorButton = getContrastColor(MaterialTheme.colorScheme.onBackground)
    ){ onClick() }
}

/**
 * Action button to stop/pause the running timer.
 */
@Composable
fun StopButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    TimerButton(
        modifier = modifier,
        iconButton = Icons.Filled.Stop,
        title = stringResource(R.string.timer_button_stop),
        colorButton = MaterialTheme.colorScheme.error,
        contentColorButton = getContrastColor(MaterialTheme.colorScheme.error)
    ){ onClick() }
}

/**
 * Action button to finish the session and record data.
 */
@Composable
fun FinishButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    TimerButton(
        modifier = modifier,
        iconButton = Icons.Filled.Check,
        title = stringResource(R.string.timer_button_finish),
        colorButton = MaterialTheme.colorScheme.onSurface,
        contentColorButton = getContrastColor(MaterialTheme.colorScheme.onSurface)
    ){ onClick() }
}