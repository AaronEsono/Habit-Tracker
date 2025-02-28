package aeb.proyecto.settings

import aeb.proyecto.settings.components.button.ButtonSettings
import aeb.proyecto.settings.components.dialog.DialogSettings
import aeb.proyecto.settings.components.divider.CustomHorizontalDivider
import aeb.proyecto.settings.constants.SettingsConstants
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.utils.openLink
import aeb.proyecto.settings.utils.sendEmail
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.text.TitleMediumText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// Cambio menos brusco al cambiar de idioma
// Poner selected
// Testing

@Composable
fun SettingsScreen(
    onImportScreen: () -> Unit,
    onSaveScreen: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val settingsDialogState = viewModel.settingDialogState.collectAsStateWithLifecycle().value

    SettingsScreen(
        onClickTheme = { viewModel.setDataDialogMode(DataDialog.THEME) },
        onClickLanguage = { viewModel.setDataDialogMode(DataDialog.LANGUAGE) },
        onClickExport = { (if (viewModel.getCurrentUser()) onSaveScreen else onImportScreen)() },
        onClickEmail = { sendEmail(context) },
        onClickGithub = { uri -> openLink(context, uri) },
        onClickLinkedin = { uri -> openLink(context, uri) }
    )

    if (settingsDialogState.showDialog) {
        DialogSettings(dataDialog = settingsDialogState.dataDialog,
            onDismissRequest = { viewModel.setStateDialog(false) },
            onClickButton = { dataResult -> viewModel.treatResultDialog(dataResult) })
    }
}

@Composable
internal fun SettingsScreen(
    onClickTheme: () -> Unit,
    onClickLanguage: () -> Unit,
    onClickExport: () -> Unit,
    onClickEmail: () -> Unit,
    onClickGithub: (String) -> Unit,
    onClickLinkedin: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            leadingIcon = R.drawable.ic_palette,
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            onClick = { onClickTheme() }
        )

        CustomHorizontalDivider()

        ButtonSettings(
            title = R.string.settings_language,
            leadingIcon = R.drawable.ic_language,
            onClick = { onClickLanguage() }
        )

        CustomHorizontalDivider()

        ButtonSettings(
            title = R.string.settings_export_import,
            leadingIcon = R.drawable.ic_save,
            shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
            onClick = { onClickExport() }
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
            onClick = { onClickEmail() }
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
    }
}