package aeb.proyecto.settings.components.dialog

import aeb.proyecto.settings.R
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DialogSettings(
    onDismissRequest: () -> Unit
) {

    CustomDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { onDismissRequest() },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleLargeText(
                stringResource(R.string.settings_theme_pick),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

}