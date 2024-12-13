package aeb.proyecto.habittracker.ui.components.dailyHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.text.LabelLargeText
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.theme.DarKThemeText
import aeb.proyecto.habittracker.ui.theme.borderTextField
import aeb.proyecto.habittracker.ui.theme.colorBackgroundCard
import aeb.proyecto.habittracker.ui.theme.secondaryColorApp
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Constans.dayOfWeek
import aeb.proyecto.habittracker.utils.Constans.requiredDays
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing3
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate

@SuppressLint("NewApi", "UnrememberedMutableInteractionSource")
@Composable
fun CardDailyHabit(
    habitWithDailyHabit: HabitWithDailyHabit,
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

    val items = rememberUpdatedState(getPrintDays())

    val icon = rememberUpdatedState(getIcon(habitWithDailyHabit.dailyHabits, habitWithDailyHabit.habit))

    val unit = remember { mutableStateOf(Constans.Units.entries.find { it.id == habit.unit }?.pluralTitle) }

    val times = rememberUpdatedState(
        dailyHabits.find { LocalDate.parse(it.date) == LocalDate.now() }?.timesDone
            ?: 0
    )

    val targetProgress = getProgress(dailyHabits, habit)

    val precomputedItems = rememberUpdatedState(getPrecomputedItems(items.value, dailyHabits, habit)).value

    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorBackgroundCard,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = spacing8)
            .border(0.3.dp, borderTextField, RoundedCornerShape(spacing8))
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                onClickCard(habit.id)
            }
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            CardDailyHabitHeader(
                isInDialog = isInDialog,
                habit = habit,
                times = times,
                unit = unit,
                icon = icon,
                targetProgress = targetProgress,
                onClick = onClick,
            )

            CardDailyHabitDays(
                precomputedItems = precomputedItems
            )

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