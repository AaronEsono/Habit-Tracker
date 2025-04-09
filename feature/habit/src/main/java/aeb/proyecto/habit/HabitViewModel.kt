package aeb.proyecto.habit

import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
):ViewModel() {

    val habits = habitWithDailyHabitRepo.getHabits()

}

sealed class HabitUIState(){
    data object Success:HabitUIState()
    data object Error:HabitUIState()
    data object Loading:HabitUIState()
}