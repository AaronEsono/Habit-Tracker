package aeb.proyecto.settings

import aeb.proyecto.settings.components.horizontal.HorizontalSettingsScreen
import aeb.proyecto.settings.components.vertical.VerticalSettingsScreen
import aeb.proyecto.settings.constants.SettingsConstants
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.utils.openLink
import aeb.proyecto.settings.utils.openOverlayPermissionSettings
import aeb.proyecto.settings.utils.sendEmail
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Main screen for the Settings module.
 * Orchestrates the UI state, dialog handling, and orientation-specific layouts.
 *
 * @param onImportScreen Callback to navigate to the Import/Export screen.
 * @param onSaveScreen Callback to navigate to the Save/Sync screen.
 * @param onAttributionsScreen Callback to navigate to the Attributions screen.
 * @param viewModel The Hilt-injected ViewModel managing settings business logic.
 */
@Composable
fun SettingsScreen(
    onImportScreen: () -> Unit,
    onSaveScreen: () -> Unit,
    onAttributionsScreen: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val orientation = getOrientation()

    val settingsDialogState = viewModel.settingDialogState.collectAsStateWithLifecycle().value
    val settingsUIState = viewModel.settingsUIState.collectAsStateWithLifecycle().value

    // Screen Title Configuration
    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.settings_configuration),fontSize = 20.sp)
    }

    // Interaction Handlers
    val onClickTheme = { viewModel.setDataDialogMode(DataDialog.THEME) }
    val onClickLanguage = { viewModel.setDataDialogMode(DataDialog.LANGUAGE) }
    val onClickGeneralSettings = { viewModel.setDataDialogMode(DataDialog.DAY_WEEK) }
    val onClickOverlay = { openOverlayPermissionSettings(context) }
    val onClickExport = { (if (viewModel.getCurrentUser()) onSaveScreen else onImportScreen)() }
    val onClickEmail = { sendEmail(context) }
    val onClickGithub = { uri: String -> openLink(context, uri) }
    val onClickTerms = { uri: String -> openLink(context, uri) }
    val onClickAttributions = { onAttributionsScreen() }
    val onClickPrivacy = { uri: String ->  openLink(context, uri) }
    val onDismissDialog = { viewModel.setStateDialog(false) }
    val onAcceptDialog = { dataResult: DataResult -> viewModel.treatResultDialog(dataResult) }

    // Orientation Logic
    when(orientation){
        Orientation.Portrait -> {
            VerticalSettingsScreen(
                settingsUIState = settingsUIState,
                dialogState = settingsDialogState,
                onClickTheme = onClickTheme,
                onClickLanguage = onClickLanguage,
                onClickGeneralSettings = onClickGeneralSettings,
                onClickOverlay = onClickOverlay,
                onClickExport = onClickExport,
                onClickEmail = onClickEmail,
                onClickGithub = onClickGithub,
                onClickTerms = onClickTerms,
                onClickAttributions = onClickAttributions,
                onClickPrivacy = onClickPrivacy,
                onDismissDialog = onDismissDialog,
                onAcceptDialog = onAcceptDialog
            )
        }
        Orientation.Landscape -> {
            HorizontalSettingsScreen(
                settingsUIState = settingsUIState,
                dialogState = settingsDialogState,
                onClickTheme = onClickTheme,
                onClickLanguage = onClickLanguage,
                onClickGeneralSettings = onClickGeneralSettings,
                onClickOverlay = onClickOverlay,
                onClickExport = onClickExport,
                onClickEmail = onClickEmail,
                onClickGithub = onClickGithub,
                onClickTerms = onClickTerms,
                onClickAttributions = onClickAttributions,
                onClickPrivacy = onClickPrivacy,
                onDismissDialog = onDismissDialog,
                onAcceptDialog = onAcceptDialog
            )
        }
    }
}