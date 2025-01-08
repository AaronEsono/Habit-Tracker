package aeb.proyecto.habittracker.ui.screens.settings

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.ui.screens.settings.settingsComponents.SettingsAbout
import aeb.proyecto.habittracker.ui.screens.settings.settingsComponents.SettingsApp
import aeb.proyecto.habittracker.ui.screens.settings.settingsComponents.SettingsCard
import aeb.proyecto.habittracker.utils.Constans.LINKEDN
import aeb.proyecto.habittracker.utils.Constans.URIGITHUB
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing24
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import aeb.proyecto.habittracker.utils.MainLocalViewModel
import aeb.proyecto.habittracker.utils.sendEmail
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel(), onImportScreen: () -> Unit, onSaveScreen:() -> Unit){

    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val mainViewModel = MainLocalViewModel.current

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = spacing16, vertical = spacing24)){

        SettingsApp(onSave = {
            settingsViewModel.checkUser(
                onSaveScreen = { onSaveScreen() },
                onImportScreen = { onImportScreen() })
        }, onTheme = {
            showDialog.value = true
        })

        Spacer(modifier = Modifier.padding(vertical = spacing12))

        SettingsAbout( onContact = {
            sendEmail(context)
        }, onCode = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URIGITHUB))
            context.startActivity(intent)
        }, onLinkedn = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LINKEDN))
            context.startActivity(intent)
        })

        BodySmallText(text = stringResource(R.string.settings_screen_version, "1.0"),
           modifier = Modifier.padding(top = spacing8))
    }

    if (showDialog.value){
        SettingsCard(onDismiss = {
            showDialog.value = false
        }, onSaveDataStore = {mode ->
            mainViewModel.saveMode(mode)
        })
    }
}