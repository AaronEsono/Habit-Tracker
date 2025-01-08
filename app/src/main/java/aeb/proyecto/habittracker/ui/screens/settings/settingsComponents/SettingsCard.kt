package aeb.proyecto.habittracker.ui.screens.settings.settingsComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.LabelLargeText
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.ui.theme.AppTheme
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing6
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SettingsCard(
    onDismiss: () -> Unit,
    onSaveDataStore: (Int) -> Unit
) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.padding(horizontal = spacing8)
                .border(1.dp, MaterialTheme.colorScheme.surfaceVariant,CardDefaults.shape),
            elevation = CardDefaults.cardElevation(
                defaultElevation = spacing8
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = spacing8, horizontal = spacing16),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TitleSmallText(
                    text = stringResource(R.string.settings_screen_card_title),
                    modifier = Modifier.fillMaxWidth().padding(top = spacing8)
                )

                Spacer(modifier = Modifier.padding(vertical = spacing8))

                AppTheme.entries.forEach {
                    CardTheme(onClick = {
                        onSaveDataStore(it.theme)
                        onDismiss()
                    }, stringResource = it.text)
                }
            }
        }
    }
}

@Composable
fun CardTheme(
    onClick: () -> Unit,
    stringResource: Int
) {
    Card(
        modifier = Modifier
            .padding(vertical = spacing6)
            .clip(CardDefaults.shape)
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant,CardDefaults.shape)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = spacing8
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.width(150.dp).padding(spacing8), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            LabelLargeText(stringResource(stringResource))
        }
    }
}