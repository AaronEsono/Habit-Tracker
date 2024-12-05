package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CardInfoAddHabit(
    onClick: () -> Unit = {},
    icon: ImageVector? = null,
    @StringRes title: Int,
    finalIcon: ImageVector? = null,
    color: MutableState<Color>,
    modifier: Modifier = Modifier
) {

    Card(
        shape = RoundedCornerShape(spacing8),
        modifier = modifier
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = spacing8
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(45.dp).padding(spacing8), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            icon?.let {
                Icon(icon, "", tint = color.value)
            }

            Spacer(modifier = Modifier.padding(horizontal = spacing8))
            TitleSmallText(stringResource(title), Modifier.weight(1f), TextAlign.Left)

            finalIcon?.let {
                Icon(finalIcon, "")
            }
        }
    }

}