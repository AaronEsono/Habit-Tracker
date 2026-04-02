package aeb.proyecto.statistics.model

import java.time.LocalDate

data class GoalsDoneState(
    val numberOfDaysCompleted:Int = 0,
    val numberOfBestStreak:Int = 0,
    val bestStreakDates: Pair<LocalDate, LocalDate> = Pair(LocalDate.now(), LocalDate.now()),
    val numberOfCurrentStreak: Int = 0,
    val currentStreakDates: Pair<LocalDate, LocalDate> = Pair(LocalDate.now(), LocalDate.now()),
)