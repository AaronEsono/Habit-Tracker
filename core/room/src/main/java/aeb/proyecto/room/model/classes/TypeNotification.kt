package aeb.proyecto.room.model.classes

import java.time.DayOfWeek

sealed class TypeNotification(val tag:String) {
    data class Daily(val days: List<DayOfWeek> = listOf(DayOfWeek.MONDAY)) : TypeNotification(DAILY_TAG)
    data class Recurring(val interval: Int = 1) : TypeNotification(RECURRING_TAG)
}