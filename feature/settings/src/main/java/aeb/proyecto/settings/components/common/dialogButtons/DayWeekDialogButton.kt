package aeb.proyecto.settings.components.common.dialogButtons

import aeb.proyecto.settings.components.common.button.BodyMediumTextButtonDialog
import aeb.proyecto.settings.components.common.button.ButtonDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.utils.setContainerColorButton
import aeb.proyecto.ui.date.DaysWeek
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun DayWeekDialogButton(
    dayOfWeek: DaysWeek,
    daySelected:String,
    onClickButton: (DataResult) -> Unit
){
    ButtonDialog(
        modifier = Modifier.padding(vertical = spacing3),
        containerColor = setContainerColorButton(dayOfWeek.id, daySelected),
        onClick = { onClickButton(DataResult.DayOfWeekResult(dayOfWeek.id)) }
    ){
        BodyMediumTextButtonDialog(text = stringResource(dayOfWeek.string))
    }
}