package aeb.proyecto.statistics.components.common.boxDays

import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.DayBoxState
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.YearMonth


//Faltaria por hacer el dia de la semana

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun StatisticsBoxDays(
    modifier: Modifier = Modifier,
    boxUIState: List<BoxUIState>,
    yearMonth: YearMonth,
    colorHabit: Color,
    startDayOfWeek: DayOfWeek,
    verticalSpacing: Dp = spacing4,
    horizontalSpacing: Dp = spacing4
) {

    val weeks = remember (boxUIState){ boxUIState.chunked(7) }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = (weeks.size - 1).coerceAtLeast(0)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing6)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(spacing6)
            )
            .background(MaterialTheme.colorScheme.surfaceTint)
    ) {

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = spacing6,
                    end = spacing6,
                    top = spacing4,
                    bottom = spacing4
                )
        ) {

            val squareSize = (maxHeight - verticalSpacing * 6 - spacing4 * 2) / 7

            LazyRow(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(
                    items = weeks,
                    key = { week -> week.hashCode()}
                ) { week ->

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(
                            verticalSpacing,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        week.forEach { box ->
                            Box(
                                modifier = Modifier
                                    .size(squareSize)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (box.dayState) {
                                            DayBoxState.Done -> colorHabit
                                            DayBoxState.NotDone -> colorHabit.copy(alpha = 0.1f)
                                            DayBoxState.Uncompleted -> colorHabit.copy(alpha = 0.4f)
                                        }
                                    )
                            )
                        }

                        val emptyDays = 7 - week.size

                        repeat(emptyDays) {
                            Spacer(Modifier.size(squareSize))
                        }
                    }

                }
            }

        }
    }

}