package aeb.proyecto.habittracker.ui.components.dailyHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.theme.DarKThemeText
import aeb.proyecto.habittracker.ui.theme.textColors
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing3
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing6
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun CardDailyHabitDays(
    color: Color,
    habit: HabitWithDailyHabit
) {

    val daysBeforeToday = rememberUpdatedState(getDaysOfWeek())
    val listState = rememberLazyListState()

    LaunchedEffect(daysBeforeToday.value) {
        listState.scrollToItem(daysBeforeToday.value.size - 1)
    }

    Spacer(modifier = Modifier.padding(top = spacing12))

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = spacing4),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(spacing4)
    ) {
        items(
            items = daysBeforeToday.value,
            key = { it.toString() }
        ) {
            val animatedProgress by animateFloatAsState(
                targetValue = getProgressDaily(habit.dailyHabits, it, habit.habit),
                animationSpec = tween(
                    durationMillis = 800, // Duración de la animación
                    easing = LinearOutSlowInEasing // Curva de animación
                ), label = ""
            )

            val animatedColor by animateColorAsState(
                targetValue = if (animatedProgress == 1f) color.copy(alpha = 0.5f) else color.copy(alpha = 0.2f),
                animationSpec = tween(
                    durationMillis = 500, // Duración de la animación
                    easing = FastOutSlowInEasing // Curva de animación
                ), label = ""
            )

            Column(
                modifier = Modifier
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabelSmallText(text = stringResource(getDay(it)))

                Spacer(modifier = Modifier.padding(vertical = spacing2))

                Box(
                    modifier = Modifier
                        .size(40.dp) // Tamaño del círculo
                        .background(
                            animatedColor,
                            shape = CircleShape
                        ), // Fondo circular
                    contentAlignment = Alignment.Center // Centrar el contenido
                ) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = color,
                        strokeWidth = spacing3,
                        trackColor = Color.Transparent,
                    )

                    LabelMediumText(
                        text = "${it.dayOfMonth}",
                        color = DarKThemeText,
                    )

                    if(animatedProgress == 1f){
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = "",
                            Modifier.size(30.dp),
                            tint = DarKThemeText.copy(alpha = 0.6f),
                        )
                    }
                }
            }
        }
    }
}