package aeb.proyecto.settings.components.dialog

import aeb.proyecto.settings.R
import aeb.proyecto.settings.components.button.ButtonDialog
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.TitleMediumText
import aeb.proyecto.ui.theme.EnumTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DialogSettings(
    onDismissRequest: () -> Unit,
    onClickButton: (Int) -> Unit
) {

    CustomDialog(
        modifier = Modifier.fillMaxWidth(0.8f),
        onDismissRequest = { onDismissRequest() },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = spacing12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = spacing8)
            ) {
                Image(
                    painter = painterResource(R.drawable.im_theme),
                    contentDescription = "image dialog",
                    modifier = Modifier
                        .size(85.dp)
                        .align(Alignment.Center)
                )

                CustomRipple{
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "close button",
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.TopEnd)
                            .clickable { onDismissRequest() },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            TitleMediumText(
                stringResource(R.string.settings_theme_pick),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing6),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            EnumTheme.entries.forEach { entrie ->
                ButtonDialog(
                    modifier = Modifier.padding(vertical = spacing2),
                    text = stringResource(entrie.text),
                    onClick = { onClickButton(entrie.theme) }
                )
            }
        }
    }
}