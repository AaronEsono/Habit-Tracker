package aeb.proyecto.room.model.classes

import java.time.LocalDate

sealed class TypeHabit(val tag: String) {
    data object Daily : TypeHabit(DAILY_TAG)
    data class Weekly(val numberDays: Int) : TypeHabit(WEEKLY_TAG)
    data class Monthly(val numberTimes: Int) : TypeHabit(MONTHLY_TAG)
    data class Recurring(val date: LocalDate, val interval: Int) : TypeHabit(RECURRING_TAG)
}

const val DAILY_TAG = "DAILY"
const val WEEKLY_TAG = "WEEKLY"
const val MONTHLY_TAG = "MONTHLY"
const val RECURRING_TAG = "RECURRING"
