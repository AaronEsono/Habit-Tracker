package aeb.proyecto.statistics.model

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit

sealed class StatisticsState{
    data object Loading: StatisticsState()
    data class Error(val message: String): StatisticsState()
    data class Success(val state: StatisticsSuccessState): StatisticsState()
}

sealed class StatisticsSuccessState{
    data object Empty: StatisticsSuccessState()
    data class Habits(val habits: List<Habit>, val habitSelected: HabitWithDailyHabit): StatisticsSuccessState()
}