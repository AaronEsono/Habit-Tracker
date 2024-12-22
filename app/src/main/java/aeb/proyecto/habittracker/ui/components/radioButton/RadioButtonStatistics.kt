package aeb.proyecto.habittracker.ui.components.radioButton

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.ui.components.dailyHabit.iconByName
import aeb.proyecto.habittracker.ui.theme.DarKThemeText
import aeb.proyecto.habittracker.ui.theme.containerTextFieldColor
import aeb.proyecto.habittracker.utils.Dimmens.spacing10
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonStatistics(
    habit: Habit,
    selectedOption: Habit,
    onClick: (Habit) -> Unit
) {

    val animatedColor by animateColorAsState(
        if (habit == selectedOption) Color(habit.color) else containerTextFieldColor,
        label = "color"
    )

    Box(
        modifier = Modifier
            .padding(horizontal = spacing4)
            .wrapContentSize()
            .clip(CircleShape)
            .border(0.5.dp, DarKThemeText, CircleShape)
            .background(animatedColor, CircleShape)
            .selectable(
                selected = (habit == selectedOption),
                onClick = {
                    onClick(habit)
                },
                role = Role.RadioButton
            )
    ) {
        Icon(
            iconByName(habit.icon), "", modifier = Modifier
                .padding(spacing10)
                .size(25.dp)
                .align(Alignment.Center),
            tint = DarKThemeText
        )
    }
}
