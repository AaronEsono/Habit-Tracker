package aeb.proyecto.habit.components.bottomSheet.editHabitDay

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetEditHabitDay(
    habit: Habit,
    habitDay: HabitDay,
    onDismiss: () -> Unit = {}
){

    CustomBottomSheet (
        onDismiss = onDismiss
    ){

    }

}