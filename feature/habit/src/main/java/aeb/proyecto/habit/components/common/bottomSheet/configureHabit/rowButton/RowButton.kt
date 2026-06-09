package aeb.proyecto.habit.components.common.bottomSheet.configureHabit.rowButton

import aeb.proyecto.habit.R
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

/**
 * Dual-action button control row used to commit or reset progress logs inside configuration panels.
 * Features a high-contrast dynamic acceptance button that adapts its foreground typography
 * readability based on the habit's primary color theme.
 *
 * @param isEnabled Flag controlling the transactional acceptance trigger availability.
 * @param color The habit-specific brand color used as the container background for the acceptance action.
 * @param onClick Execution callback for the primary commit transaction.
 * @param onClickRestart Execution callback for the data-purge/reset transaction.
 */
@Composable
fun RowButton(
    isEnabled:Boolean,
    color:Color,
    onClick: () -> Unit,
    onClickRestart: () -> Unit,
){
    Row (
        modifier = Modifier.fillMaxWidth().padding(bottom = spacing12, top = spacing10),
        verticalAlignment = Alignment.CenterVertically
    ){

        // Reset/Restart action button
        CustomRipple {
            Button(
                modifier = Modifier.weight(1f),
                onClick = onClickRestart,
                shape = RoundedCornerShape(spacing8),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                LabelLargeText(
                    stringResource(R.string.habit_restart),
                    color = MaterialTheme.colorScheme.inverseOnSurface)
            }
        }

        // Accept/Commit action button with dynamic contrast color calculation
        CustomRipple {
            Button(
                modifier = Modifier.padding(start = spacing10).weight(1f),
                onClick = onClick,
                enabled = isEnabled,
                shape = RoundedCornerShape(spacing8),
                colors = ButtonDefaults.buttonColors(
                    containerColor = color
                )
            ) {
                LabelLargeText(
                    stringResource(R.string.habit_accept),
                    // Ensures high-contrast readability against the dynamic habit color
                    color = getContrastColor(color)
                )
            }
        }
    }
}