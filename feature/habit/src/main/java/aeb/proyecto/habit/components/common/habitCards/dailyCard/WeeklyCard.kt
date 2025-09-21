package aeb.proyecto.habit.components.common.habitCards.dailyCard

import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDate

@Composable
fun WeeklyCard(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    habit: HabitWithDailyHabit,
    onClick: (id:Long,date: LocalDate) -> Unit,
    onLongClick: (id:Long,date: LocalDate) -> Unit
){

    LabelLargeText("Hola")

}