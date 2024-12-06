package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.theme.DarKThemeText
import aeb.proyecto.habittracker.ui.theme.primaryColorApp
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CardPickUnitAddHabit(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    selected: Boolean,
    unit: Constans.Units,
    color: MutableState<Color>
) {

    Card(
        shape = RoundedCornerShape(spacing8),
        modifier = modifier
            .wrapContentWidth()
            .clickable { onClick() }
            .border(if (selected) 2.dp else 0.dp, if (selected) color.value else DarKThemeText, RoundedCornerShape(spacing8))
    ) {
        Row(
            modifier = Modifier
                .height(40.dp)
                .wrapContentWidth()
                .background(primaryColorApp)
                .padding(horizontal = spacing8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(unit.icon),
                "",
                modifier = Modifier.size(25.dp),
                tint = DarKThemeText
            )

            Spacer(modifier = Modifier.padding(horizontal = spacing4))

            LabelMediumText(stringResource(unit.title))
        }
    }
}

@Preview
@Composable
fun CardPickUnitAddHabitPreview() {
    CardPickUnitAddHabit(
        selected = false,
        unit = Constans.Units.PAGES,
        color =  remember { mutableStateOf(Color.Red) }
    )
}