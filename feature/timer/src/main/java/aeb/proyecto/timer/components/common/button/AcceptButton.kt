package aeb.proyecto.timer.components.common.button

import aeb.proyecto.timer.R
import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.TitleMediumText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A primary action button for starting the timer service.
 * Features haptic feedback and context-aware enabled/disabled states.
 *
 * @param timerUIState The current success state containing configuration and button enablement status.
 * @param onStartService Callback triggered when the button is pressed.
 */
@Composable
fun AcceptButton(
    timerUIState: TimerUiState.Success,
    onStartService: () -> Unit,
){

    val haptic = LocalHapticFeedback.current

    CustomRipple {
        Button(
            onClick = {
                onStartService()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(spacing12),
            enabled = timerUIState.timerDataUIState.buttonEnabled,
            modifier = Modifier.testTag("timer_accept_button_start_service")
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Icon(
                    Icons.Filled.PlayCircleOutline,
                    contentDescription = "icon start",
                    tint = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.size(20.dp)
                )

                TitleMediumText(
                    stringResource(R.string.timer_start),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.padding(horizontal = spacing12, vertical = spacing2)
                )
            }
        }
    }
}