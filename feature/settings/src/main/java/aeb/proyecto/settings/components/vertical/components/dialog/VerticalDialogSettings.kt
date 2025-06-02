package aeb.proyecto.settings.components.vertical.components.dialog

import aeb.proyecto.settings.components.commom.button.BodyMediumTextButtonDialog
import aeb.proyecto.settings.components.commom.button.ButtonDialog
import aeb.proyecto.settings.components.commom.dialogButtons.DayWeekDialogButton
import aeb.proyecto.settings.components.commom.dialogButtons.LanguageDialogButton
import aeb.proyecto.settings.components.commom.dialogButtons.ThemeDialogButton
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.model.DialogElements
import aeb.proyecto.settings.utils.setContainerColorButton
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.TitleMediumText
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun VerticalDialogSettings(
    dataDialog: DataDialog,
    themeSelected:Int,
    languageSelected:String,
    daySelected:String,
    onDismissRequest: () -> Unit,
    onClickButton: (DataResult) -> Unit
){
    CustomDialog(
        modifier = Modifier.fillMaxWidth(0.8f),
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = spacing12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing8)
            ) {
                Image(
                    painter = painterResource(dataDialog.image),
                    contentDescription = "image dialog",
                    modifier = Modifier
                        .size(85.dp)
                        .align(Alignment.Center)
                )

                CustomRipple{
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "close button",
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.TopEnd)
                            .clickable { onDismissRequest() },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            TitleMediumText(
                stringResource(dataDialog.title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing6, start = spacing8, end = spacing8),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                when(dataDialog.dialogComponent){
                    is DialogElements.DialogLanguage -> {
                        dataDialog.dialogComponent.language.forEach { elementLanguage ->
                            LanguageDialogButton(
                                elementLanguage = elementLanguage,
                                languageSelected = languageSelected,
                                onClickButton = onClickButton
                            )
                        }
                    }
                    is DialogElements.DialogTheme -> {
                        dataDialog.dialogComponent.theme.forEach { elementTheme ->
                            ThemeDialogButton(
                                elementTheme = elementTheme,
                                themeSelected = themeSelected,
                                onClickButton = onClickButton
                            )
                        }
                    }

                    is DialogElements.DialogDayWeek -> {
                        dataDialog.dialogComponent.dayWeek.forEach { dayOfWeek ->
                            DayWeekDialogButton(
                                dayOfWeek = dayOfWeek,
                                daySelected = daySelected,
                                onClickButton = onClickButton
                            )
                        }
                    }
                }
            }
        }
    }
}