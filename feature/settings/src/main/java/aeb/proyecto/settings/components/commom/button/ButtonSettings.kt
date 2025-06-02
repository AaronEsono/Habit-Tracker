package aeb.proyecto.settings.components.commom.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelSmallText
import aeb.proyecto.ui.text.TitleSmallText
import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun ButtonSettings(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    @DrawableRes leadingIcon: Int,
    @StringRes title: Int,
    @StringRes label:Int? = null,
    onClick: () -> Unit
) {

    CustomRipple{
        ElevatedButton(
            onClick = { onClick() },
            shape = shape,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = modifier.height(65.dp),
            contentPadding = PaddingValues(spacing10)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(leadingIcon),
                    "icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(25.dp)
                )

                Column (
                    modifier = Modifier.weight(1f).padding(start = spacing10),
                    verticalArrangement = Arrangement.Center
                ){
                    TitleSmallText(stringResource(title))

                    label?.let {
                        LabelSmallText(stringResource(label),
                            modifier = Modifier.padding(end = spacing10))
                    }
                }

                Icon(
                    Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "end icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}