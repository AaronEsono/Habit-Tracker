package aeb.proyecto.settings.components.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.TitleSmallText
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ButtonSettings(
    modifier : Modifier = Modifier,
    shape: Shape = ShapeDefaults.Medium,
    @DrawableRes leadingIcon: Int,
    @StringRes title: Int,
    onClick: () -> Unit
) {

    Button(
        onClick = { onClick() },
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = spacing4,
            pressedElevation = spacing4,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier,
        contentPadding = PaddingValues(spacing10)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacing2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(leadingIcon), "icon", tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp)
            )

            TitleSmallText(
                stringResource(title),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = spacing10)
            )

            Icon(
                Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = "end icon",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}