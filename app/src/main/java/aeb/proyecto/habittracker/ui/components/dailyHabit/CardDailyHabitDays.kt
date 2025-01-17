package aeb.proyecto.habittracker.ui.components.dailyHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.utils.Constans.visibleItems
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing3
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun CardDailyHabitDays(
    color: Color,
    habit: HabitWithDailyHabit,
    getDaysOfWeek: List<LocalDate>,
    isInDialog:Boolean = false
) {

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = getDaysOfWeek.size - 1)

    Spacer(modifier = Modifier.padding(top = spacing12))

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val totalHorizontalPadding = spacing4 * 2
        val totalSpacing = spacing4 * (visibleItems - 1)
        val itemWidth = (maxWidth - totalHorizontalPadding - totalSpacing) / visibleItems

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clipToBounds()
                .padding(horizontal = spacing4),
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(spacing4)
        ) {
            items(
                items = getDaysOfWeek,
                key = { it.toString() }
            ) {
                val animatedProgress by animateFloatAsState(
                    targetValue = getProgressDaily(habit.dailyHabits, it, habit.habit),
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = LinearOutSlowInEasing
                    ), label = ""
                )

                val animatedColor by animateColorAsState(
                    targetValue = if (animatedProgress == 1f) color.copy(alpha = 0.6f) else color.copy(
                        alpha = 0.2f
                    ),
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
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
                            .size(itemWidth)
                            .background(
                                animatedColor,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.fillMaxSize(),
                            color = color,
                            strokeWidth = spacing3,
                            trackColor = color.copy(alpha = 0.1f),
                            gapSize = 0.dp
                        )

                        if (animatedProgress != 1f) {
                            LabelMediumText(
                                text = "${it.dayOfMonth}",
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }

                        if (animatedProgress == 1f) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = "",
                                Modifier.size(if (isInDialog) 20.dp else 30.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            )
                        }
                    }
                }
            }
        }
    }

}