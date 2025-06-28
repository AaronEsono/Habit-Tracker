package aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.card.CardEditDay
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen.incompleteCases.HourIncompleteMode
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen.incompleteCases.UnitIncompleteMode
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.halfTimesLeft
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.timesLeft
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.model.classes.unitsHourMode
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun IncompleteDay(
    habit: Habit,
    habitDay: HabitDay,
    onRestart:(id:Long,date:LocalDate) -> Unit,
    onClickTimer:(Triple<Long,String,BigDecimal>) -> Unit,
    onClick:(id:Long, date: LocalDate, goalDone:BigDecimal) -> Unit
){

    val timesLeft = remember { timesLeft(habit.goal, habitDay.goalDone) }
    val halfTimesLeft = remember { halfTimesLeft(timesLeft, habit.unit) }

    /**Informacion seleccion y faltantes*/
    /** Si quedan habitos por hacer, esta pantalla */
    LabelLargeText(
        stringResource(R.string.habit_edit_habit_day_times_label),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing16)
    )

    when{
        habit.unit !in unitsHourMode -> {
            UnitIncompleteMode(
                habit = habit,
                day = habitDay,
                leftTimes = timesLeft,
                halfTimesLeft = halfTimesLeft,
                onRestart = { id, date -> onRestart(id,date) },
                onClickTimer = onClickTimer,
                onClick =  { id, date, goalDone -> onClick(id,date,goalDone)}
            )
        }
        else -> {
            HourIncompleteMode(
                habit = habit,
                day = habitDay,
                leftTimes = timesLeft,
                halfTimesLeft = halfTimesLeft,
                onRestart = { id, date -> onRestart(id,date) },
                onClickTimer = onClickTimer,
                onClick =  { id, date, goalDone -> onClick(id,date,goalDone)}
            )
        }
    }

}