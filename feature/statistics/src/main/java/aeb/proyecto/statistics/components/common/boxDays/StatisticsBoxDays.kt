package aeb.proyecto.statistics.components.common.boxDays

import aeb.proyecto.statistics.R
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.DayBoxState
import aeb.proyecto.statistics.utils.getWeeks
import aeb.proyecto.statistics.utils.label
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.text.BodySmallText
import aeb.proyecto.ui.text.LabelSmallText
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.YearMonth


//Faltaria por hacer cuadrar el tamaño de los textos de los dias

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun StatisticsBoxDays(
    modifier: Modifier = Modifier,
    boxUIState: List<BoxUIState>,
    colorHabit: Color,
    startDayOfWeek: DayOfWeek,
    verticalSpacing: Dp = spacing4,
    horizontalSpacing: Dp = spacing4
) {

    val weeks = getWeeks(
        boxUIState = boxUIState,
        startDayOfWeek = startDayOfWeek
    )

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = (weeks.size - 1).coerceAtLeast(0)
    )

    val orderedDays = remember(startDayOfWeek) {
        val days = DayOfWeek.entries
        val startIndex = days.indexOf(startDayOfWeek)

        (0 until 7).map { i ->
            days[(startIndex + i) % 7]
        }
    }

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

            val density = LocalDensity.current

            val textSize = with(density) {
                (squareSize * 0.85f).toSp()
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (
                    modifier = Modifier.fillMaxHeight().fillMaxWidth(0.05f),
                    verticalArrangement = Arrangement.spacedBy(
                        verticalSpacing,
                        Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    orderedDays.forEach { day ->
                        TextDayWeek(
                            text = stringResource(day.label()),
                            size = squareSize,
                            textSize = textSize
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(horizontal = spacing1))

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
}