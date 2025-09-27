package aeb.proyecto.habit.components.common.bottomSheet.configureHabit.screen

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.screen.incompleteCases.HourIncompleteMode
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.screen.incompleteCases.UnitIncompleteMode
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.utils.halfTimesLeft
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.utils.timesLeft
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.unitsHourMode
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun IncompleteDay(
    habitWithDay: HabitWithDay,
    onRestart:(id:Long,date:LocalDate) -> Unit,
    onClickTimer:(Triple<Long,String,BigDecimal>) -> Unit,
    onClick:(id:Long, date: LocalDate, goalDone:BigDecimal) -> Unit
){

    val timesLeft = remember { timesLeft(habitWithDay.habit.goal, habitWithDay.day.goalDone) }
    val halfTimesLeft = remember { halfTimesLeft(timesLeft, habitWithDay.habit.unit) }

    /**Informacion seleccion y faltantes*/
    /** Si quedan habitos por hacer, esta pantalla */
    LabelLargeText(
        stringResource(R.string.habit_edit_habit_day_times_label),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing16)
    )

    when{
        habitWithDay.habit.unit !in unitsHourMode -> {
            UnitIncompleteMode(
                habitWithDay = habitWithDay,
                leftTimes = timesLeft,
                halfTimesLeft = halfTimesLeft,
                onRestart = { id, date -> onRestart(id,date) },
                onClickTimer = onClickTimer,
                onClick =  { id, date, goalDone -> onClick(id,date,goalDone)}
            )
        }
        else -> {
            HourIncompleteMode(
                habitWithDay = habitWithDay,
                leftTimes = timesLeft,
                halfTimesLeft = halfTimesLeft,
                onRestart = { id, date -> onRestart(id,date) },
                onClickTimer = onClickTimer,
                onClick =  { id, date, goalDone -> onClick(id,date,goalDone)}
            )
        }
    }

}