package aeb.proyecto.settings.components.common.dialogButtons

import aeb.proyecto.settings.components.common.button.BodyMediumTextButtonDialog
import aeb.proyecto.settings.components.common.button.ButtonDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.utils.getSelectionContainerColor
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.theme.EnumTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource

/**
 * Button component for selecting the application theme within a dialog.
 *
 * @param elementTheme The [EnumTheme] option this button represents.
 * @param themeSelected The integer ID of the currently selected theme.
 * @param onClickButton Callback to return the selected [DataResult.ThemeResult].
 */
@Composable
fun ThemeDialogButton(
    elementTheme: EnumTheme,
    themeSelected:Int,
    onClickButton: (DataResult) -> Unit
){
    ButtonDialog(
        modifier = Modifier
            .padding(vertical = spacing3)
            .testTag("dialog_theme_option_${elementTheme.theme}"),
        containerColor = getSelectionContainerColor(elementTheme.theme, themeSelected),
        onClick = { onClickButton(DataResult.ThemeResult(elementTheme.theme)) }
    ){
        BodyMediumTextButtonDialog(text = stringResource(elementTheme.title))
    }
}