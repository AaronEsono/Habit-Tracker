package aeb.proyecto.habit.components.horizontal.components.bottomSheet.configureHabit

import aeb.proyecto.room.entities.relations.HabitWithDay
import androidx.compose.runtime.Composable
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun HorizontalConfigureHabitBottomSheet(
    habitWithDay: HabitWithDay,
    onDismiss: () -> Unit = {},
    onRestart:(id:Long,date: LocalDate) -> Unit,
    onClickTimer: (Triple<Long,String, BigDecimal>) -> Unit,
    onClick:(id:Long, date: LocalDate, goalDone: BigDecimal) -> Unit
){

}