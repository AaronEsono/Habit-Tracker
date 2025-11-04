package aeb.proyecto.habit.components.common.bottomSheet.editHabit.state

import aeb.proyecto.room.entities.Habit


sealed class EditHabitState{
    data class Error (val error: String): EditHabitState()
    data object Loading: EditHabitState()
    data class Success (val habit: Habit): EditHabitState()
}