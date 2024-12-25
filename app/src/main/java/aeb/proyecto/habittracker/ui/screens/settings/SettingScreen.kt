package aeb.proyecto.habittracker.ui.screens.settings

import aeb.proyecto.habittracker.ui.screens.settings.settingsComponents.SettingsAbout
import aeb.proyecto.habittracker.ui.screens.settings.settingsComponents.SettingsApp
import aeb.proyecto.habittracker.utils.Constans.LINKEDN
import aeb.proyecto.habittracker.utils.Constans.URIGITHUB
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing24
import aeb.proyecto.habittracker.utils.sendEmail
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel()){

    val context = LocalContext.current

    Column (modifier = Modifier.fillMaxSize().padding(horizontal = spacing16, vertical = spacing24)){

        SettingsApp()

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
    }
}
