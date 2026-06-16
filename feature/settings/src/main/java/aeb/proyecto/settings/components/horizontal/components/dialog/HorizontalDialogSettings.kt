package aeb.proyecto.settings.components.horizontal.components.dialog

import aeb.proyecto.settings.components.common.dialogButtons.DayWeekDialogButton
import aeb.proyecto.settings.components.common.dialogButtons.LanguageDialogButton
import aeb.proyecto.settings.components.common.dialogButtons.ThemeDialogButton
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.model.DialogElements
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.TitleMediumText
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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

/**
 * A reusable dialog component that adapts its content based on the provided [DataDialog].
 *
 * @param dataDialog The configuration defining which type of options to display.
 * @param themeSelected The current theme ID.
 * @param languageSelected The current language string code.
 * @param daySelected The current start-of-week day string.
 * @param onDismissRequest Lambda to trigger when the user clicks outside the dialog.
 * @param onClickButton Callback to return the selected [DataResult] to the ViewModel.
 */
@Composable
fun HorizontalDialogSettings(
    dataDialog: DataDialog,
    themeSelected:Int,
    languageSelected:String,
    daySelected:String,
    onDismissRequest: () -> Unit,
    onClickButton: (DataResult) -> Unit
){

    CustomDialog(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
            .wrapContentHeight()
            .padding(horizontal = spacing8),
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.primary
    ) {

        Column {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = spacing12, top = spacing12, bottom = spacing8),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ){
                CustomRipple{
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "close button",
                        modifier = Modifier
                            .size(35.dp)
                            .clickable { onDismissRequest() },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row (
                modifier = Modifier
                    .padding(bottom = spacing12),
                verticalAlignment = Alignment.CenterVertically
            ){
                Column (
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = spacing8)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){

                    TitleMediumText(
                        stringResource(dataDialog.title),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.padding(vertical = spacing2))

                    Image(
                        painter = painterResource(dataDialog.image),
                        contentDescription = "image dialog",
                        modifier = Modifier
                            .size(100.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = spacing6))

                Column (
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = spacing8)
                        .wrapContentHeight()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    when (dataDialog.dialogComponent) {
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
}