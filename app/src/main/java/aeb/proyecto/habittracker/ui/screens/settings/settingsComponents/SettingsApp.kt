package aeb.proyecto.habittracker.ui.screens.settings.settingsComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.card.CardOptionButton
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SettingsApp(
    onTheme: () -> Unit,
    onSave: () -> Unit
) {


    TitleSmallText(
        text = stringResource(R.string.settings_screen_app),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing4), textAlign = TextAlign.Start
    )

    CardOptionButton(
        title = R.string.settings_screen_theme,
        startIcon = R.drawable.ic_palette_bold,
        shape = RoundedCornerShape(topStart = spacing8, topEnd = spacing8),
    ) {
        onTheme()
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.outline)

    CardOptionButton(
        title = R.string.settings_screen_save,
        startIcon = R.drawable.ic_save,
        shape = RoundedCornerShape(bottomStart = spacing8, bottomEnd = spacing8)
    ) {
        onSave()
    }
}