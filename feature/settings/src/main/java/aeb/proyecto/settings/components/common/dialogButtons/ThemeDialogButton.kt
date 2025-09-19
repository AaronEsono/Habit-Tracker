package aeb.proyecto.settings.components.common.dialogButtons

import aeb.proyecto.settings.components.common.button.BodyMediumTextButtonDialog
import aeb.proyecto.settings.components.common.button.ButtonDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.utils.setContainerColorButton
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.theme.EnumTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun ThemeDialogButton(
    elementTheme: EnumTheme,
    themeSelected:Int,
    onClickButton: (DataResult) -> Unit
){
    ButtonDialog(
        modifier = Modifier.padding(vertical = spacing3),
        containerColor = setContainerColorButton(elementTheme.theme, themeSelected),
        onClick = { onClickButton(DataResult.ThemeResult(elementTheme.theme)) }
    ){
        BodyMediumTextButtonDialog(text = stringResource(elementTheme.title))
    }
}