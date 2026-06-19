package aeb.proyecto.timer.components.common.habitLinked.states

import aeb.proyecto.timer.model.HabitLinkedState
import aeb.proyecto.ui.date.utils.getTextToday
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Displays the currently linked habit information within the Timer screen.
 * Shows the habit icon, its name, the target date, and provides a clear
 * action to unlink (remove) the habit.
 *
 * @param modifier Applied to the [Row] container.
 * @param linkedState The [HabitLinkedState.Data] containing habit and day details.
 * @param onClickCross Callback triggered when the user taps the 'clear' icon.
 */
@Composable
fun LinkedHabit(
    modifier: Modifier = Modifier,
    linkedState: HabitLinkedState.Data,
    onClickCross:()->Unit = {}
){
    Row (
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Box(
            modifier = Modifier
                .padding(start = spacing12)
                .size(35.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(spacing8))
                .background(
                    Color(linkedState.data.habit.color).copy(alpha = 0.75f),
                    RoundedCornerShape(spacing8)
                ),
            contentAlignment = Alignment.Center
        ){
            Icon(
                linkedState.data.habit.icon,
                contentDescription = "habit icon pick habit",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(spacing4).fillMaxSize(1f)
            )
        }

        Column (
            modifier = Modifier.padding(start = spacing16).weight(1f),
            verticalArrangement = Arrangement.Center
        ){
            LabelLargeText(linkedState.data.habit.name)
            LabelMediumText(getTextToday(linkedState.data.day.date))
        }


        CustomRipple {
            Icon(
                Icons.Filled.Clear,
                contentDescription = "habit icon pick habit",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = spacing8)
                    .size(35.dp)
                    .clickable {
                        onClickCross()
                    }
            )
        }
    }
}