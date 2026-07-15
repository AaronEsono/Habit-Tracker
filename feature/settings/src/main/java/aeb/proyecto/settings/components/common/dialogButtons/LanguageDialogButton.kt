package aeb.proyecto.settings.components.common.dialogButtons

import aeb.proyecto.language.model.EnumLanguage
import aeb.proyecto.settings.components.common.button.BodyMediumTextButtonDialog
import aeb.proyecto.settings.components.common.button.ButtonDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.utils.getSelectionContainerColor
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * Button component for selecting the application language within a dialog.
 * Includes a visual icon (flag) and the language name.
 *
 * @param elementLanguage The [EnumLanguage] option this button represents.
 * @param languageSelected The code of the currently selected language.
 * @param onClickButton Callback to return the selected [DataResult.LanguageResult].
 */
@Composable
fun LanguageDialogButton(
    elementLanguage: EnumLanguage,
    languageSelected:String,
    onClickButton: (DataResult) -> Unit
){
    ButtonDialog(
        modifier = Modifier
            .padding(vertical = spacing3)
            .testTag("dialog_language_option_${elementLanguage.value}"),
        paddingValues = PaddingValues(horizontal = spacing6),
        containerColor = getSelectionContainerColor(elementLanguage.value, languageSelected),
        onClick = { onClickButton(DataResult.LanguageResult(elementLanguage.value)) }
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(elementLanguage.image),
                "image",
                modifier = Modifier.size(22.dp)
            )

            BodyMediumTextButtonDialog(text = stringResource(elementLanguage.title))
        }
    }
}