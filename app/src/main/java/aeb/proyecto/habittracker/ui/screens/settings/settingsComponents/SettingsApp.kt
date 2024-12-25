package aeb.proyecto.habittracker.ui.screens.settings.settingsComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.card.CardOptionButton
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.ui.theme.borderTextField
import aeb.proyecto.habittracker.utils.Constans.ENDBUTTON
import aeb.proyecto.habittracker.utils.Constans.NOBORDER
import aeb.proyecto.habittracker.utils.Constans.STARTBUTTON
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SettingsApp(){


    TitleSmallText(text = stringResource(R.string.settings_screen_app),
        modifier = Modifier.fillMaxWidth().padding(vertical = spacing4), textAlign = TextAlign.Start)

    CardOptionButton(title = R.string.settings_screen_theme, icon = R.drawable.ic_palette_bold, border =  STARTBUTTON){}
    HorizontalDivider(color = borderTextField)
    CardOptionButton(title = R.string.settings_screen_save, icon = R.drawable.ic_save, border =  ENDBUTTON){}
}