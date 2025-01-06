package aeb.proyecto.habittracker.ui.components.buttons

import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedButton(
    modifier: Modifier = Modifier,
    modifierIcon: Modifier = Modifier,
    onClick: () -> Unit = {},
    shape: RoundedCornerShape = RoundedCornerShape(spacing12),
    colorIcon: Color = MaterialTheme.colorScheme.onSurface,
    colorText : Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.onSurface,
    @StringRes title: Int,
    @DrawableRes icon: Int? = null
) {
    OutlinedButton(
        onClick = { onClick() },
        shape = shape,
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        icon?.let {
            Icon(
                painter = painterResource(icon), "customOutlinedButton",
                tint = colorIcon,
                modifier = modifierIcon.size(20.dp)
            )

            Spacer(modifier = Modifier.padding(horizontal = spacing4))
        }

        BodySmallText(text = stringResource(title), color = colorText)
    }
}