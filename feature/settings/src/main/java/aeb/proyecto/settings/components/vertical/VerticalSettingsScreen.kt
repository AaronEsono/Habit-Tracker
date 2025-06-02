package aeb.proyecto.settings.components.vertical

import aeb.proyecto.language.model.returnStringValue
import aeb.proyecto.settings.BuildConfig
import aeb.proyecto.settings.R
import aeb.proyecto.settings.SettingsUIState
import aeb.proyecto.settings.components.commom.button.ButtonSettings
import aeb.proyecto.settings.components.commom.divider.CustomHorizontalDivider
import aeb.proyecto.settings.components.commom.loading.SettingsLoading
import aeb.proyecto.settings.components.vertical.components.dialog.VerticalDialogSettings
import aeb.proyecto.settings.constants.SettingsConstants
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.model.SettingsDialogState
import aeb.proyecto.ui.date.utils.getDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleMediumText
import aeb.proyecto.ui.theme.getTitle
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSettingsScreen(
    settingsUIState: SettingsUIState,
    dialogState: SettingsDialogState,
    onClickTheme: () -> Unit,
    onClickLanguage: () -> Unit,
    onClickGeneralSettings: () -> Unit,
    onClickExport: () -> Unit,
    onClickEmail: () -> Unit,
    onClickGithub: (String) -> Unit,
    onClickLinkedin: (String) -> Unit,
    onDismissDialog:() -> Unit,
    onAcceptDialog:(DataResult) -> Unit
){

    when(settingsUIState){
        SettingsUIState.Error -> Unit
        SettingsUIState.Loading -> {
            SettingsLoading()
        }
        is SettingsUIState.Success -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = spacing24, start = spacing16, end = spacing16)
            ) {

                TitleMediumText(
                    text = stringResource(R.string.settings_configuration),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )

                ButtonSettings(
                    modifier = Modifier.padding(top = spacing6),
                    title = R.string.settings_theme,
                    label = getTitle(settingsUIState.data.theme),
                    leadingIcon = R.drawable.ic_palette,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    onClick = onClickTheme
                )

                CustomHorizontalDivider()

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    ButtonSettings(
                        title = R.string.settings_language,
                        leadingIcon = R.drawable.ic_language,
                        label = returnStringValue(settingsUIState.data.language),
                        onClick = onClickLanguage
                    )

                    CustomHorizontalDivider()
                }

                ButtonSettings(
                    title = R.string.settings_day_title,
                    label = getDay(settingsUIState.data.dayOfWeek),
                    leadingIcon = R.drawable.ic_calendar_day,
                    onClick = onClickGeneralSettings
                )

                CustomHorizontalDivider()

                ButtonSettings(
                    title = R.string.settings_export_import,
                    leadingIcon = R.drawable.ic_save,
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                    onClick = onClickExport
                )

                Spacer(modifier = Modifier.padding(vertical = spacing16))

                TitleMediumText(
                    text = stringResource(R.string.settings_about),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )

                ButtonSettings(
                    modifier = Modifier.padding(top = spacing6),
                    title = R.string.settings_email,
                    leadingIcon = R.drawable.ic_email,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    onClick = onClickEmail
                )

                CustomHorizontalDivider()

                ButtonSettings(
                    title = R.string.settings_github,
                    leadingIcon = R.drawable.ic_github,
                    onClick = { onClickGithub(SettingsConstants.LINK_GITHUB) }
                )

                CustomHorizontalDivider()

                ButtonSettings(
                    title = R.string.settings_link,
                    leadingIcon = R.drawable.ic_link,
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                    onClick = { onClickLinkedin(SettingsConstants.LINK_LINKEDN) }
                )

                Spacer(modifier = Modifier.padding(vertical = spacing6))

                LabelMediumText(stringResource(R.string.settings_version, BuildConfig.APP_VERSION))
            }

            if(dialogState.showDialog){
                VerticalDialogSettings(
                    dataDialog = dialogState.dataDialog,
                    themeSelected = settingsUIState.data.theme,
                    languageSelected = settingsUIState.data.language,
                    daySelected = settingsUIState.data.dayOfWeek,
                    onDismissRequest = onDismissDialog,
                    onClickButton = { dataResult -> onAcceptDialog(dataResult) }
                )
            }
        }
    }
}