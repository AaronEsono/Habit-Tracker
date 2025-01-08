package aeb.proyecto.habittracker.ui.components.dailyHabit

import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@SuppressLint("NewApi", "UnrememberedMutableInteractionSource")
@Composable
fun CardDailyHabit(
    habitWithDailyHabit: HabitWithDailyHabit,
    borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    onClick: (Long) -> Unit = {},
    onClickCard: (Long) -> Unit = {},
    isInDialog: Boolean = false,
    onCancelClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {}
) {

    val habit = rememberUpdatedState(habitWithDailyHabit.habit).value
    val dailyHabits = rememberUpdatedState(habitWithDailyHabit.dailyHabits).value

    val icon = rememberUpdatedState(getIcon(habitWithDailyHabit.dailyHabits, habitWithDailyHabit.habit))
    val getDays = rememberUpdatedState(getDaysOfWeek())

    val unit = remember { mutableStateOf(Constans.Units.entries.find { it.id == habit.unit }?.pluralTitle) }
    val times = rememberUpdatedState(dailyHabits.find { LocalDate.parse(it.date) == LocalDate.now() }?.timesDone ?: 0)

    val targetProgress = getProgress(dailyHabits, habit)

    Card(
        shape = RoundedCornerShape(spacing8),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = spacing8)
            .border(0.3.dp, borderColor, RoundedCornerShape(spacing8))
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                onClickCard(habit.id)
            }
    ) {
        Column(modifier = Modifier.wrapContentSize().padding(horizontal = spacing8)) {
            CardDailyHabitHeader(
                isInDialog = isInDialog,
                habit = habit,
                times = times,
                unit = unit,
                targetProgress = targetProgress,
                icon = icon,
                onClick = onClick,
            )

            CardDailyHabitDays(
                color = Color(habit.color),
                habit = habitWithDailyHabit,
                getDaysOfWeek = getDays.value,
                isInDialog = isInDialog
            )

            Spacer(modifier = Modifier.padding(spacing4))

            if (isInDialog) {
                CardDailyHabitDialog(
                    onCancelClick = onCancelClick,
                    onDeleteClick = onDeleteClick,
                    onEditClick = onEditClick,
                    onCalendarClick = onCalendarClick
                )
            }
        }
    }
}

@Preview
@Composable
fun CardDailyHabitPreview() {
    CardDailyHabit(HabitWithDailyHabit())
}