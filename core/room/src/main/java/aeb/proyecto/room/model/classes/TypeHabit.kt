package aeb.proyecto.room.model.classes

import java.time.LocalDate

sealed class TypeHabit(val tag: String) {
    data object Daily : TypeHabit(DAILY)
    data class Weekly(val numberDays: Int) : TypeHabit(WEEKLY)
    data class Monthly(val numberTimes: Int) : TypeHabit(MONTHLY)
    data class Recurring(val date: LocalDate, val interval: Int) : TypeHabit(RECURRING)
}

const val DAILY = "DAILY"
const val WEEKLY = "WEEKLY"
const val MONTHLY = "MONTHLY"
const val RECURRING = "RECURRING"
