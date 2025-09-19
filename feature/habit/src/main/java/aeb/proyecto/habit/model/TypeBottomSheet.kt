package aeb.proyecto.habit.model

import aeb.proyecto.room.entities.Habit

sealed class TypeBottomSheet {
    data object SelectDate: TypeBottomSheet()
    data class EditHabitDay(val habit: Habit): TypeBottomSheet()
}