package aeb.proyecto.habittracker.ui.screens.settings

import aeb.proyecto.habittracker.ui.screens.settings.settingsComponents.SettingsAbout
import aeb.proyecto.habittracker.ui.screens.settings.settingsComponents.SettingsApp
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing24
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel()){

    Column (modifier = Modifier.fillMaxSize().padding(horizontal = spacing16, vertical = spacing24)){
        SettingsApp()
        Spacer(modifier = Modifier.padding(vertical = spacing12))
        SettingsAbout()
    }

}

