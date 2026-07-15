package aeb.proyecto.settings.components.common.dialogButtons

import aeb.proyecto.settings.components.common.button.BodyMediumTextButtonDialog
import aeb.proyecto.settings.components.common.button.ButtonDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.utils.getSelectionContainerColor
import aeb.proyecto.ui.date.DaysWeek
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource

/**
 * Button component for selecting the start day of the week within a dialog.
 *
 * @param dayOfWeek The specific [DaysWeek] option this button represents.
 * @param daySelected The string representation of the currently selected day.
 * @param onClickButton Callback to return the selected [DataResult.DayOfWeekResult].
 */
@Composable
fun DayWeekDialogButton(
    dayOfWeek: DaysWeek,
    daySelected:String,
    onClickButton: (DataResult) -> Unit
){
    ButtonDialog(
        modifier = Modifier
            .padding(vertical = spacing3)
            .testTag("dialog_day_option_${dayOfWeek.id}"),
        containerColor = getSelectionContainerColor(dayOfWeek.id.toString(), daySelected),
        onClick = { onClickButton(DataResult.DayOfWeekResult(dayOfWeek.id)) }
    ){
        BodyMediumTextButtonDialog(text = stringResource(dayOfWeek.string))
    }
}