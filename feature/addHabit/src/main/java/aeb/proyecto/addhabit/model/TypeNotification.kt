package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.R
import androidx.annotation.StringRes
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

data class AddHabitNotification(
    val id:String = UUID.randomUUID().toString(),
    val time:LocalTime = LocalTime.now(),
    val type:TypeNotification = TypeNotification.Daily()
)

sealed class TypeNotification{
    data class Daily(val days:MutableList<Int> = mutableListOf(1)):TypeNotification()
    data class Recurring(val interval:Int = 1):TypeNotification()
}

enum class TypeNotifications(
    @StringRes val title: Int,
    @StringRes val label: Int,
    val type: TypeNotification
) {
    DAILY(
        R.string.add_habit_daily_notification_title,
        R.string.add_habit_daily_notification_label,
        TypeNotification.Daily()
    ),
    CYCLIC(
        R.string.add_habit_cyclic_notification_title,
        R.string.add_habit_cyclic_notification_label,
        TypeNotification.Recurring()
    )
}

sealed class TypeNotificationResult{
    data class Daily(val day:Int, val id:String):TypeNotificationResult()
    data class Recurring(val action:Boolean, val id:String):TypeNotificationResult()
}

val DEFAULT_TIME = AddHabitNotification(
    id = "-1",
    time = LocalTime.now(),
    type = TypeNotification.Daily()
)