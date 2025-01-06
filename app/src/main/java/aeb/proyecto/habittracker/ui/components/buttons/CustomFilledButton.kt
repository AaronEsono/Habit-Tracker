package aeb.proyecto.habittracker.ui.components.buttons

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.utils.ColorsTheme
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    @StringRes title: Int,
    @DrawableRes icon: Int?,
    color:Color,
    enabled: Boolean = true,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation()
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        enabled = enabled,
        elevation = elevation,
        shape = RoundedCornerShape(spacing12),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = ColorsTheme.colorDisabled,
        )
    ) {
        icon?.let {
            Icon(
                painter = painterResource(icon), "",
                tint = ColorsTheme.themeText,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.padding(horizontal = spacing4))

        BodySmallText(text = stringResource(title))
    }
}

@Preview
@Composable
fun CustomFilledButtonPreview() {
    CustomFilledButton(title = R.string.buttons_accept, icon = R.drawable.ic_check, color = Color.Red)
}