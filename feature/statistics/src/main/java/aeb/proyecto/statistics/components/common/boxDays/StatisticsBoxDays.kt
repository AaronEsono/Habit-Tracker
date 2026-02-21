package aeb.proyecto.statistics.components.common.boxDays

import aeb.proyecto.statistics.utils.TOTAL_DAYS
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.YearMonth

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun StatisticsBoxDays(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
    colorHabit: Color,
    startDayOfWeek: DayOfWeek,
    totalDays: Int = TOTAL_DAYS,
    verticalSpacing: Dp = spacing4,
    horizontalSpacing: Dp = spacing4
){

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = totalDays - 1
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
                verticalAlignment = Alignment.CenterVertically,
            ) {

                items(totalDays) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        repeat(7) {
                            Box(
                                modifier = Modifier
                                    .size(squareSize)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(colorHabit.copy(alpha = 0.5f))
                            )
                        }
                    }
                }
            }
        }
    }

}