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

@Composable
fun RowButton(
    isEnabled:Boolean,
    color:Color,
    onClick: () -> Unit,
    onClickRestart: () -> Unit,
){
    /** Botones */
    Row (
        modifier = Modifier.fillMaxWidth().padding(bottom = spacing12, top = spacing10),
        verticalAlignment = Alignment.CenterVertically
    ){

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
                    color = getContrastColor(color)
                )
            }
        }
    }
}