package aeb.proyecto.habit.components.common.bottomSheet.selectDate

import aeb.proyecto.habit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * A horizontal layout component containing action buttons for a BottomSheet.
 *
 * This component provides a "Cancel" button and a "Today" action button.
 * It manages the sheet state by triggering a hide animation within the
 * provided [CoroutineScope] before executing the respective dismiss or
 * selection callbacks.
 *
 * @param modifier Modifier to be applied to the row container.
 * @param scope The [CoroutineScope] used to launch the sheet dismissal animation.
 * @param sheetState The [SheetState] of the current BottomSheet to control its visibility.
 * @param onDismiss Callback invoked when the actions are finalized or cancelled.
 * @param onClick Callback invoked when the "Today" button is pressed,
 * providing the current date and a boolean flag.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowButtonSelectDate(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    sheetState: SheetState,
    onDismiss: () -> Unit = {},
    onClick: (LocalDate, Boolean) -> Unit = {_,_ ->}
){

    Row (
        modifier = modifier.fillMaxWidth()
            .padding(start = spacing16, bottom = spacing10, end = spacing16, top = spacing4)
    ){
        CustomRipple {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(spacing12),
                modifier = Modifier.weight(1f).padding(end = spacing12),
            ) {
                LabelLargeText(stringResource(R.string.habit_cancel))
            }
        }

        CustomRipple {
            Button(
                onClick = {
                    onClick(LocalDate.now(),true)
                    scope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                shape = RoundedCornerShape(spacing12),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            ) {
                LabelLargeText(
                    stringResource(R.string.habit_today_title),
                    color = MaterialTheme.colorScheme.inverseOnSurface)
            }
        }
    }
}