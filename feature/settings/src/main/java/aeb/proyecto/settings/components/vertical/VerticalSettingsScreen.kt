package aeb.proyecto.settings.components.vertical

import aeb.proyecto.language.model.returnStringValue
import aeb.proyecto.settings.BuildConfig
import aeb.proyecto.settings.R
import aeb.proyecto.settings.SettingsUIState
import aeb.proyecto.settings.components.common.button.ButtonSettings
import aeb.proyecto.settings.components.common.divider.CustomHorizontalDivider
import aeb.proyecto.settings.components.common.loading.SettingsLoading
import aeb.proyecto.settings.components.vertical.components.dialog.VerticalDialogSettings
import aeb.proyecto.settings.constants.SettingsConstants
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.model.SettingsDialogState
import aeb.proyecto.settings.utils.OnChangeOverlay
import aeb.proyecto.ui.date.utils.getDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleMediumText
import aeb.proyecto.ui.theme.getTitle
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A vertical-optimized screen for application settings.
 * Displays configuration options, overlay management, and external support links
 * in a vertical layout.
 *
 * @param settingsUIState The current state of the settings data ([SettingsUIState.Success], [Loading], [Error]).
 * @param dialogState The state defining whether a dialog is visible and which [DataDialog] to display.
 * @param onClickTheme Callback to trigger the theme selection dialog.
 * @param onClickLanguage Callback to trigger the language selection dialog.
 * @param onClickGeneralSettings Callback to trigger the general settings (e.g., start day of the week).
 * @param onClickOverlay Callback to navigate to the system overlay permission screen.
 * @param onClickExport Callback to navigate to the import/export screen.
 * @param onClickEmail Callback to open the email client with pre-filled device info.
 * @param onClickGithub Callback to open the project repository in a browser, receiving the URL.
 * @param onClickTerms Callback to open the profile in a browser, receiving the URL.
 * @param onClickAttributions Callback to navigate to the attributions screen.
 * @param onClickPrivacy Callback to navigate to the privacy policy screen.
 * @param onDismissDialog Callback to close the currently active dialog.
 * @param onAcceptDialog Callback to process the result ([DataResult]) selected by the user in a dialog.
 */
@Composable
fun VerticalSettingsScreen(
    settingsUIState: SettingsUIState,
    dialogState: SettingsDialogState,
    onClickTheme: () -> Unit,
    onClickLanguage: () -> Unit,
    onClickGeneralSettings: () -> Unit,
    onClickOverlay: () -> Unit,
    onClickExport: () -> Unit,
    onClickEmail: () -> Unit,
    onClickGithub: (String) -> Unit,
    onClickTerms: (String) -> Unit,
    onClickAttributions: () -> Unit,
    onClickPrivacy: (String) -> Unit,
    onDismissDialog:() -> Unit,
    onAcceptDialog:(DataResult) -> Unit
){

    when(settingsUIState){
        SettingsUIState.Error -> Unit
        SettingsUIState.Loading -> {
            SettingsLoading()
        }
        is SettingsUIState.Success -> {

            val context = LocalContext.current
            val overlayActivated = remember { mutableStateOf(Settings.canDrawOverlays(context)) }
            OnChangeOverlay(overlayActivated, context)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = spacing24, start = spacing16, end = spacing16, bottom = spacing8)
            ) {

                TitleMediumText(
                    text = stringResource(R.string.settings_configuration),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )

                ButtonSettings(
                    modifier = Modifier
                        .padding(top = spacing6)
                        .testTag("settings_button_theme"),
                    title = R.string.settings_theme,
                    label = getTitle(settingsUIState.data.themeMode),
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
                        onClick = onClickLanguage,
                        modifier = Modifier.testTag("settings_button_language")
                    )

                    CustomHorizontalDivider()
                }

                ButtonSettings(
                    title = R.string.settings_day_title,
                    label = getDay(settingsUIState.data.dayStartWeek),
                    leadingIcon = R.drawable.ic_calendar_day,
                    onClick = onClickGeneralSettings,
                    modifier = Modifier.testTag("settings_button_dayWeek")
                )

                CustomHorizontalDivider()

                ButtonSettings(
                    title = R.string.settings_overlay,
                    leadingIcon = R.drawable.ic_overlay,
                    label = if(overlayActivated.value) R.string.settings_enabled else R.string.settings_disabled,
                    onClick = onClickOverlay,
                    modifier = Modifier.testTag("settings_button_overlay")
                )

                CustomHorizontalDivider()

                ButtonSettings(
                    title = R.string.settings_export_import,
                    leadingIcon = R.drawable.ic_save,
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                    onClick = onClickExport,
                    modifier = Modifier.testTag("settings_button_export")
                )

                Spacer(modifier = Modifier.padding(vertical = spacing16))

                TitleMediumText(
                    text = stringResource(R.string.settings_about),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )

                ButtonSettings(
                    modifier = Modifier
                        .padding(top = spacing6)
                        .testTag("settings_button_email"),
                    title = R.string.settings_email,
                    leadingIcon = R.drawable.ic_email,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    onClick = onClickEmail
                )

                CustomHorizontalDivider()

                ButtonSettings(
                    title = R.string.settings_github,
                    leadingIcon = R.drawable.ic_github,
                    onClick = { onClickGithub(SettingsConstants.LINK_GITHUB) },
                    modifier = Modifier.testTag("settings_button_github")
                )

                CustomHorizontalDivider()

                ButtonSettings(
                    title = R.string.settings_attributions,
                    leadingIcon = R.drawable.ic_attributions,
                    onClick = { onClickAttributions() },
                    modifier = Modifier.testTag("settings_button_attributions")
                )

                CustomHorizontalDivider()

                ButtonSettings(
                    title = R.string.settings_terms,
                    leadingIcon = R.drawable.ic_terms,
                    onClick = { onClickTerms(SettingsConstants.LINK_TERMS) },
                    modifier = Modifier.testTag("settings_button_terms")
                )

                CustomHorizontalDivider()

                ButtonSettings(
                    title = R.string.settings_privacy,
                    leadingIcon = R.drawable.ic_privacy,
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                    onClick = { onClickPrivacy(SettingsConstants.LINK_POLICY) },
                    modifier = Modifier.testTag("settings_button_privacy")
                )

                Spacer(modifier = Modifier.padding(vertical = spacing6))

                LabelMediumText(stringResource(R.string.settings_version, BuildConfig.APP_VERSION))
            }

            if(dialogState.showDialog){
                VerticalDialogSettings(
                    dataDialog = dialogState.dataDialog,
                    themeSelected = settingsUIState.data.themeMode,
                    languageSelected = settingsUIState.data.language,
                    daySelected = settingsUIState.data.dayStartWeek,
                    onDismissRequest = onDismissDialog,
                    onClickButton = { dataResult -> onAcceptDialog(dataResult) }
                )
            }
        }
    }
}