package aeb.proyecto.habit.components.screen

import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.FilteredHabitsUiState
import aeb.proyecto.habit.TimeRangeUiState
import aeb.proyecto.habit.components.loading.HabitLoading
import aeb.proyecto.habit.components.screen.typeHabits.DailyHabitsScreen
import aeb.proyecto.habit.components.screen.typeHabits.MonthlyHabitsScreen
import aeb.proyecto.habit.components.timeRange.DailyTimeRange
import aeb.proyecto.habit.components.timeRange.MonthlyTimeRange
import aeb.proyecto.habit.components.timeRange.WeeklyTimeRange
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.time.LocalDate

/**
 *  Pantalla para mostrar los hábitos de un tipo en específico.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerElementScreen(
    pagerElements: List<PagerElement>,
    filteredHabitsUIState: FilteredHabitsUiState,
    currentPagerSelected: CurrentPagerSelection,
    selectedTimeRangeUiState: TimeRangeUiState,
    selectedDate: LocalDate = LocalDate.now(),
    onClickTab: (PagerElement) -> Unit = {},
    onClickTimeRange: (LocalDate) -> Unit = {},
    onLongClick: (id:Long,date:LocalDate) -> Unit,
    onClick: (id: Long, date: LocalDate) -> Unit
){

    val selectedTabIndex = when (currentPagerSelected) {
        is CurrentPagerSelection.Selected -> currentPagerSelected.pagerSelected.index
        else -> 0 // O un índice predeterminado si no está inicializado
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ){

        //Mostramos los tipos de hábitos en la tabRow
        PrimaryTabRow(selectedTabIndex = selectedTabIndex,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = true),
                    width = Dp.Unspecified,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            divider = {
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
            },
            containerColor = MaterialTheme.colorScheme.surfaceTint
        ) {
            pagerElements.forEachIndexed { index, pagerElement ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onClickTab(pagerElement) },
                    text = {
                        LabelMediumText(
                            stringResource(pagerElement.title),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontSize = getTextSizePager(pagerElements.size)
                        )
                    }
                )
            }
        }

        //Aqui mostramos los rangos de las fechas
        AnimatedContent(
            targetState = selectedTimeRangeUiState::class
        ) { timeRangeClass ->
            when (timeRangeClass) {
                TimeRangeUiState.Empty::class -> Unit
                TimeRangeUiState.Daily::class -> {
                    val daily = selectedTimeRangeUiState as? TimeRangeUiState.Daily ?: return@AnimatedContent
                    DailyTimeRange(selectedDate, daily.days, onClick = onClickTimeRange)
                }
                TimeRangeUiState.Weekly::class -> {
                    val weekly = selectedTimeRangeUiState as? TimeRangeUiState.Weekly ?: return@AnimatedContent
                    WeeklyTimeRange(weekly.startOfWeek, weekly.endOfWeek, onClick = onClickTimeRange)
                }
                TimeRangeUiState.Monthly::class -> {
                    val monthly = selectedTimeRangeUiState as? TimeRangeUiState.Monthly ?: return@AnimatedContent
                    MonthlyTimeRange(monthly.startOfMonth, monthly.endOfMonth, onClick = onClickTimeRange)
                }
                TimeRangeUiState.Recurring::class -> {
                    val recurring = selectedTimeRangeUiState as? TimeRangeUiState.Recurring ?: return@AnimatedContent
                    DailyTimeRange(selectedDate, recurring.days, onClick = onClickTimeRange)
                }
                else -> Unit
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)

        // Contenido de los hábitos
        when (filteredHabitsUIState) {
            is FilteredHabitsUiState.Loading, is FilteredHabitsUiState.Error, is FilteredHabitsUiState.Empty -> {
                HabitLoading()
            }
            is FilteredHabitsUiState.Success -> {
                if (currentPagerSelected is CurrentPagerSelection.Selected) {
                    when (currentPagerSelected.pagerSelected.pagerElement) {
                        PagerElement.DAILY -> {
                            DailyHabitsScreen(
                                selectedDate, filteredHabitsUIState.habits,
                                onLongClick = onLongClick,
                                onClick = onClick
                            )
                        }

                        PagerElement.WEEKLY -> {

                        }
                        PagerElement.MONTHLY -> {
                            MonthlyHabitsScreen()
                        }
                        PagerElement.RECURRING -> {

                        }
                    }
                }
            }
        }
    }
}

fun getTextSizePager(size: Int): TextUnit {
    return when (size) {
        4 -> 10.sp
        3 -> 12.sp
        else -> 14.sp
    }
}