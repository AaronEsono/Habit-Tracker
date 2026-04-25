package aeb.proyecto.statistics.components.vertical.screens

import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.statistics.R
import aeb.proyecto.statistics.components.common.boxDays.StatisticsBoxDays
import aeb.proyecto.statistics.components.common.calendar.StatisticsCalendar
import aeb.proyecto.statistics.components.common.donutChart.PieChart
import aeb.proyecto.statistics.components.common.donutChart.PieChartData
import aeb.proyecto.statistics.components.common.goalBox.GoalBoxDays
import aeb.proyecto.statistics.components.common.goalBox.GoalBoxStreak
import aeb.proyecto.statistics.components.common.graphics.hourGraphics.HourGraphics
import aeb.proyecto.statistics.components.common.graphics.monthGraphics.MonthGraphics
import aeb.proyecto.statistics.components.common.header.HeaderTitle
import aeb.proyecto.statistics.model.BoxUIState
import aeb.proyecto.statistics.model.GoalsDoneState
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.statistics.utils.dateFormatter
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import java.time.DayOfWeek
import java.time.YearMonth

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun VerticalHabitSelectedScreen(
    habitSelected: HabitWithDailyHabit,
    boxUIState: List<BoxUIState>,
    graphicsState: GraphicsState,
    hourlyGraphicsState: GraphicsState,
    goalDoneState: GoalsDoneState,
    pieChartState: List<PieChartData>,
    yearMonth: YearMonth,
    yearGraphicsSelected: Int,
    yearHourlyGraphicsSelected: Int,
    startDayOfWeek: DayOfWeek,
    calendarUIState: CalendarUIState<HabitWithDay>,
    onMonthChange: (YearMonth) -> Unit,
    onYearSelected: (Boolean) -> Unit = {},
    onHourYearSelected: (Boolean) -> Unit = {}
){


    BoxWithConstraints {
        val headerHeight = maxHeight * 0.1f
        val boxHeight = maxHeight * 0.25f
        val goalBoxHeight = maxHeight * 0.10f
        val pieChartHeight = maxHeight * 0.3f


        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(top = spacing6)
        ){

            HeaderTitle(
                habit = habitSelected.habit,
                modifier = Modifier.height(headerHeight)
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            StatisticsCalendar(
                modifier = Modifier.padding(horizontal = spacing4),
                yearMonth = yearMonth,
                startDayOfWeek = startDayOfWeek,
                calendarUIState = calendarUIState,
                onMonthChange = onMonthChange
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            if(boxUIState.isNotEmpty()){
                StatisticsBoxDays(
                    modifier = Modifier.height(boxHeight),
                    boxUIState = boxUIState,
                    colorHabit = Color(habitSelected.habit.color),
                    startDayOfWeek = startDayOfWeek,
                )
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            MonthGraphics(
                graphicsState = graphicsState,
                yearGraphicsSelected = yearGraphicsSelected,
                onYearSelected = onYearSelected
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            Row(
                modifier = Modifier
                    .padding(horizontal = spacing6)
                    .fillMaxWidth()
                    .height(goalBoxHeight)
            ) {
                GoalBoxDays(
                    modifier = Modifier.fillMaxHeight().weight(1f),
                    title = goalDoneState.numberOfDaysCompleted.toString(),
                    subTitle = stringResource(R.string.statistics_goal_subtitle_completed,)
                )

                Spacer(modifier = Modifier.padding(horizontal = spacing2))

                GoalBoxDays(
                    modifier = Modifier.fillMaxHeight().weight(1f),
                    title = stringResource(R.string.statistics_goal_title_percentage, goalDoneState.consistencyPercentage),
                    subTitle = stringResource(R.string.statistics_goal_subtitle_percentage)
                )
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            HourGraphics(
                graphicsState = hourlyGraphicsState,
                yearGraphicsSelected = yearHourlyGraphicsSelected,
                onYearSelected = onHourYearSelected
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            Row(
                modifier = Modifier
                    .padding(horizontal = spacing6)
                    .fillMaxWidth()
                    .height(goalBoxHeight)
            ) {
                GoalBoxStreak(
                    modifier = Modifier.fillMaxHeight().weight(1f),
                    title = goalDoneState.numberOfBestStreak.toString(),
                    dateString = stringResource(R.string.statistics_goal_subtitle_streak_date,
                        goalDoneState.bestStreakDates.first.format(dateFormatter),
                        goalDoneState.bestStreakDates.second.format(dateFormatter)),
                    subTitle = stringResource(R.string.statistics_goal_subtitle_streak_historic)
                )

                Spacer(modifier = Modifier.padding(horizontal = spacing2))

                GoalBoxStreak(
                    modifier = Modifier.fillMaxHeight().weight(1f),
                    title = goalDoneState.numberOfCurrentStreak.toString(),
                    dateString = stringResource(R.string.statistics_goal_subtitle_streak_date,
                        goalDoneState.currentStreakDates.first.format(dateFormatter),
                        goalDoneState.currentStreakDates.second.format(dateFormatter)),
                    subTitle = stringResource(R.string.statistics_goal_subtitle_streak_today)
                )
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            PieChart(
                data = pieChartState,
                chartHeight = pieChartHeight
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))
        }
    }

}