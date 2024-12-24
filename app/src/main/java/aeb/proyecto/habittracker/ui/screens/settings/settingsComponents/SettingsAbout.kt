package aeb.proyecto.habittracker.ui.screens.settings.settingsComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.card.CardOptionButton
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.utils.Constans.ENDBUTTON
import aeb.proyecto.habittracker.utils.Constans.NOBORDER
import aeb.proyecto.habittracker.utils.Constans.STARTBUTTON
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SettingsAbout(){

    TitleSmallText(text = stringResource(R.string.settings_screen_about),
        modifier = Modifier.fillMaxWidth().padding(vertical = spacing8), textAlign = TextAlign.Start)

    CardOptionButton(title = R.string.settings_screen_contact, icon = R.drawable.ic_email, border =  STARTBUTTON){}
    HorizontalDivider()
    CardOptionButton(title = R.string.settings_screen_code, icon = R.drawable.ic_github, border =  NOBORDER){}
    HorizontalDivider()
    CardOptionButton(title = R.string.settings_screen_linkedn, icon = R.drawable.ic_link, border =  ENDBUTTON){}
}