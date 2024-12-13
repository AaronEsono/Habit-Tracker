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

    val dimens = remember { if (isInDialog) 55.dp else 45.dp }
    val dimensIcon = remember { if (isInDialog) 35.dp else 30.dp }
    val dimensDay = remember { 12.dp }

    val habit = rememberUpdatedState(habitWithDailyHabit.habit).value
    val dailyHabits = rememberUpdatedState(habitWithDailyHabit.dailyHabits).value

    val items = rememberUpdatedState(requiredDays + getPrintDays())

    val colorIcons = remember { Color(habit.color).copy(alpha = 0.1f) }

    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = items.value
    )

    val icon =
        rememberUpdatedState(getIcon(habitWithDailyHabit.dailyHabits, habitWithDailyHabit.habit))

    val unit =
        remember { mutableStateOf(Constans.Units.entries.find { it.id == habit.unit }?.pluralTitle) }

    val times = rememberUpdatedState(
        dailyHabits.find { LocalDate.parse(it.date) == LocalDate.now() }?.timesDone
            ?: 0
    )

    val targetProgress = getProgress(
        dailyHabits,
        habit
    )

    val animatedIconSize by animateDpAsState(
        targetValue = if (targetProgress == 1f) dimensIcon - 5.dp else dimensIcon - 10.dp,  // Cambiar tamaño cuando el progreso es 100%
        animationSpec = tween(durationMillis = 500), label = ""  // Duración de la animación
    )

    val precomputedItems = remember(dailyHabits, habit, items.value) {
        val dateMap = dailyHabits.associateBy { LocalDate.parse(it.date) }

        List(items.value) { index ->
            val dateDay = LocalDate.now().minusDays((items.value - index - 1).toLong())
            getColorDay(dateDay, dateMap, habit)
        }
    }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = spacing8, end = spacing8, top = spacing8)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colorIcons,
                    ),
                    modifier = Modifier
                        .size(dimens)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = iconByName(habit.icon),
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(dimensIcon)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(dimens)
                        .padding(horizontal = spacing8),
                    verticalArrangement = Arrangement.Center
                ) {
                    LabelLargeText(habit.name)

                    habit.description?.let {
                        LabelSmallText(
                            habit.description!!,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .height(dimens)
                        .padding(horizontal = spacing8), verticalArrangement = Arrangement.Center
                ) {
                    LabelSmallText(
                        text = "${times.value}/${habit.times}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(habit.color)
                    )
                    LabelSmallText(
                        text = stringResource(unit.value ?: R.string.add_habit_unit_pl_1),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(habit.color)
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colorIcons,
                    ),
                    modifier = Modifier
                        .size(dimens)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center, // Asegura que el icono esté centrado
                            modifier = Modifier
                                .size(dimens)
                                .padding(spacing4)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) { onClick(habit.id) }
                        ) {
                            // Progreso circular
                            CircularProgressIndicator(
                                progress = { targetProgress },
                                modifier = Modifier.fillMaxSize(),
                                color = (Color(habit.color)),
                                strokeWidth = spacing3,
                                trackColor = (Color(habit.color).copy(alpha = 0.3f)),
                                gapSize = spacing2
                            )

                            Icon(
                                imageVector = icon.value,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(animatedIconSize)
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.wrapContentSize()) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(7), // Fijar 7 filas
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            (dimensDay * 7) + // Altura de las celdas
                                    (spacing4 * 6) + // Espaciado entre filas (7 filas - 1 = 6 espaciados)
                                    (spacing8 * 2)
                        )
                        .padding(spacing8),
                    state = lazyGridState,
                    horizontalArrangement = Arrangement.spacedBy(spacing3), // Espaciado entre columnas
                    verticalArrangement = Arrangement.spacedBy(spacing4) // Espaciado entre filas
                ) {
                    itemsIndexed(items = precomputedItems, key = { int, _ -> int }) { _, color ->
                        Canvas(modifier = Modifier.size(dimensDay)) {
                            drawRoundRect(
                                color = color,
                                size = size,
                                cornerRadius = CornerRadius(spacing3.toPx(), spacing3.toPx())
                            )
                        }
                    }
                }

                if (isInDialog) {
                    val size = remember { mutableStateOf(24.dp) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = spacing8, bottom = spacing8, end = spacing8)
                            .wrapContentHeight()
                    ) {

                        iconDialog(size.value, R.drawable.ic_calendar_2) {
                            onCalendarClick()
                        }

                        Spacer(modifier = Modifier.padding(end = spacing8))

                        iconDialog(size.value, R.drawable.ic_edit) {
                            onEditClick()
                        }

                        Spacer(modifier = Modifier.padding(end = spacing8))

                        iconDialog(size.value, R.drawable.ic_delete) {
                            onDeleteClick()
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        iconDialog(size.value, R.drawable.ic_cancel_2) {
                            onCancelClick()
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi", "UnrememberedMutableInteractionSource")
@Composable
fun iconDialog(size: Dp, icon: Int, oncClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .wrapContentSize()// Tamaño total del fondo redondeado
            .background(color = secondaryColorApp, shape = CircleShape) // Fondo redondo
            .padding(8.dp) // Espaciado interno opcional
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) { oncClick() }
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = DarKThemeText,
            modifier = Modifier.size(size) // Asegura que el ícono se ajuste al fondo
        )
    }
}

fun getIcon(dates: List<DailyHabit>, habit: Habit): ImageVector {
    val daily = dates.find { (LocalDate.parse(it.date)) == LocalDate.now() }

    return if (daily != null && daily.timesDone == habit.times) {
        Icons.Filled.Check
    } else
        Icons.Filled.Add

}

fun getProgress(dates: List<DailyHabit>, habit: Habit): Float {
    val dailyHabit = dates.find { (LocalDate.parse(it.date)) == LocalDate.now() }

    return if (dailyHabit != null) {
        dailyHabit.timesDone / habit.times.toFloat()
    } else {
        0f
    }
}

fun getColorDay(dateDay: LocalDate, dateMap: Map<LocalDate, DailyHabit>, habit: Habit): Color {
    val dailyHabit = dateMap[dateDay] // O(1) en lugar de O(n) con 'find'

    return if (dailyHabit != null) {
        Color(habit.color).copy(alpha = 0.1f + (1f - 0.1f) * (dailyHabit.timesDone.toFloat() / habit.times.toFloat()))
    } else {
        Color(habit.color).copy(alpha = 0.1f)
    }
}

fun getPrintDays(): Int {
    val todayDay = LocalDate.now().dayOfWeek.value
    val daySelected = dayOfWeek

    val difference = todayDay - daySelected

    return if (difference < 0) {
        8 + difference
    } else {
        difference + 1
    }
}

fun iconByName(name: String): ImageVector {
    val cl = Class.forName("androidx.compose.material.icons.filled.${name}Kt")
    val method = cl.declaredMethods.first()
    return method.invoke(null, Icons.Filled) as ImageVector
}

@Preview
@Composable
fun CardDailyHabitPreview() {
    CardDailyHabit(HabitWithDailyHabit())
}