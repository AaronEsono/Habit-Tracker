package aeb.proyecto.settings.components.dialog

import aeb.proyecto.settings.components.button.BodyMediumTextButtonDialog
import aeb.proyecto.settings.components.button.ButtonDialog
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.model.DialogElements
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek

@Composable
fun DialogSettings(
    dataDialog: DataDialog,
    themeSelected:Int,
    languageSelected:String,
    daySelected:String,
    onDismissRequest: () -> Unit,
    onClickButton: (DataResult) -> Unit
) {

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

            when(dataDialog.dialogComponent){
                is DialogElements.DialogLanguage -> {
                    dataDialog.dialogComponent.language.forEach { elementLanguage ->
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
                }
                is DialogElements.DialogTheme -> {
                    dataDialog.dialogComponent.theme.forEach { elementTheme ->
                        ButtonDialog(
                            modifier = Modifier.padding(vertical = spacing3),
                            containerColor = setContainerColorButton(elementTheme.theme, themeSelected),
                            onClick = { onClickButton(DataResult.ThemeResult(elementTheme.theme)) }
                        ){
                            BodyMediumTextButtonDialog(text = stringResource(elementTheme.title))
                        }
                    }
                }

                is DialogElements.DialogDayWeek -> {
                    dataDialog.dialogComponent.dayWeek.forEach { dayOfWeek ->
                        ButtonDialog(
                            modifier = Modifier.padding(vertical = spacing3),
                            containerColor = setContainerColorButton(dayOfWeek.id, daySelected),
                            onClick = { onClickButton(DataResult.DayOfWeekResult(dayOfWeek.id)) }
                        ){
                            BodyMediumTextButtonDialog(text = stringResource(dayOfWeek.string))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun setContainerColorButton(theme: Int, themeSelected: Int): Color {
    return if (theme == themeSelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.background
}

@Composable
fun setContainerColorButton(language: String, languageSelected: String): Color {
    return if (language == languageSelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.background
}

@Composable
fun setContainerColorButton(day:DayOfWeek, daySelected: String): Color {
    return if (day.toString() == daySelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.background
}