package aeb.proyecto.settings.components.common.dialogButtons

import aeb.proyecto.language.model.EnumLanguage
import aeb.proyecto.settings.components.common.button.BodyMediumTextButtonDialog
import aeb.proyecto.settings.components.common.button.ButtonDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.utils.setContainerColorButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun LanguageDialogButton(
    elementLanguage: EnumLanguage,
    languageSelected:String,
    onClickButton: (DataResult) -> Unit
){
    ButtonDialog(
        modifier = Modifier.padding(vertical = spacing3),
        paddingValues = PaddingValues(horizontal = spacing6),
        containerColor = setContainerColorButton(elementLanguage.value, languageSelected),
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