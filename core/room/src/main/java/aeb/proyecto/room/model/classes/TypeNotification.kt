package aeb.proyecto.room.model.classes

sealed class TypeNotification(val tag:String) {
    data class Daily(val days: List<Int> = listOf(1)) : TypeNotification(DAILY)
    data class Recurring(val interval: Int = 1) : TypeNotification(RECURRING)
}