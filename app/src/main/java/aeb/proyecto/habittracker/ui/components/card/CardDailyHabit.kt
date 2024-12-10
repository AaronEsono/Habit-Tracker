package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.text.LabelLargeText
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.theme.colorBackgroundCard
import aeb.proyecto.habittracker.utils.Constans.dayOfWeek
import aeb.proyecto.habittracker.utils.Constans.requiredDays
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing3
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate

@SuppressLint("NewApi", "UnrememberedMutableInteractionSource")
@Composable
fun CardDailyHabit(habitWithDailyHabit: HabitWithDailyHabit, onClick: (Long) -> Unit = {}) {

    val dimens = remember { mutableStateOf(45.dp) }
    val dimensIcon = remember { mutableStateOf(30.dp) }
    val dimensDay = remember { mutableStateOf(9.dp) }

    val items = rememberUpdatedState(requiredDays + getPrintDays())
    val listOfDays = rememberUpdatedState(LocalDate.now().minusDays(items.value.toLong()).datesUntil(LocalDate.now().plusDays(1)).toList())

    val colorIcons = remember { mutableStateOf(Color(habitWithDailyHabit.habit.color).copy(alpha = 0.1f)) }

    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = listOfDays.value.size
    )

    val icon = rememberUpdatedState(getIcon(habitWithDailyHabit.dailyHabits, habitWithDailyHabit.habit))

    val animatedProgress by
    animateFloatAsState(
        targetValue = rememberUpdatedState(getProgress(habitWithDailyHabit.dailyHabits, habitWithDailyHabit.habit)).value,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )

    val animatedIconSize by animateDpAsState(
        targetValue = if (animatedProgress == 1f) dimensIcon.value - 5.dp else dimensIcon.value - 10.dp,  // Cambiar tamaño cuando el progreso es 100%
        animationSpec = tween(durationMillis = 500), label = ""  // Duración de la animación
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorBackgroundCard,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = spacing8)
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
                        containerColor = colorIcons.value,
                    ),
                    modifier = Modifier
                        .size(dimens.value)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = iconByName(habitWithDailyHabit.habit.icon),
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(dimensIcon.value)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(dimens.value)
                        .padding(start = spacing8),
                    verticalArrangement = Arrangement.Center
                ) {
                    LabelLargeText(habitWithDailyHabit.habit.name)

                    habitWithDailyHabit.habit.description?.let {
                        LabelSmallText(habitWithDailyHabit.habit.description!!)
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colorIcons.value,
                    ),
                    modifier = Modifier
                        .size(dimens.value)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center, // Asegura que el icono esté centrado
                            modifier = Modifier
                                .size(dimens.value)
                                .padding(spacing4)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) { onClick(habitWithDailyHabit.habit.id) }
                        ) {
                            // Progreso circular
                            CircularProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier.fillMaxSize(),
                                color = (Color(habitWithDailyHabit.habit.color)),
                                strokeWidth = spacing3,
                                trackColor = (Color(habitWithDailyHabit.habit.color).copy(alpha = 0.3f)),
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
                            (dimensDay.value * 7) + // Altura de las celdas
                                    (spacing2 * 6) + // Espaciado entre filas (7 filas - 1 = 6 espaciados)
                                    (spacing8 * 2)
                        )
                        .padding(spacing8),
                    state = lazyGridState,
                    horizontalArrangement = Arrangement.spacedBy(spacing2), // Espaciado entre columnas
                    verticalArrangement = Arrangement.spacedBy(spacing2) // Espaciado entre filas
                ) {
                    items(listOfDays.value.size) { index ->
                        Card(
                            modifier = Modifier
                                .size(dimensDay.value), // Tamaño fijo de cada celda
                            colors = CardDefaults.cardColors(
                                containerColor = getColorDay(listOfDays.value[index], habitWithDailyHabit.dailyHabits, habitWithDailyHabit.habit)
                            ),
                            shape = RoundedCornerShape(spacing3)
                        ) {}
                    }
                }
            }
        }
    }
}

fun getIcon(dates: List<DailyHabit>,habit: Habit):ImageVector{
    val daily = dates.find { (LocalDate.parse(it.date)) == LocalDate.now() }

    return if (daily != null && daily.timesDone == habit.times) {
         Icons.Filled.Check
    }else
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

fun getColorDay(dateDay: LocalDate, dates: List<DailyHabit>, habit: Habit): Color {
    val dailyHabit = dates.find { (LocalDate.parse(it.date)) == dateDay }

    return if (dailyHabit != null) {
        Color(habit.color).copy(alpha = 0.1f + (1f - 0.1f) * ((dailyHabit.timesDone.toFloat() / habit.times.toFloat())))
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