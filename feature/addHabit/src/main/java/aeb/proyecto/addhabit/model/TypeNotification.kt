package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.R
import androidx.annotation.StringRes
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.LocalTime

data class AddHabitNotification(
    val time:LocalTime = LocalTime.now(),
    val type:TypeNotification = TypeNotification.Daily()
)

sealed class TypeNotification{
    data class Daily(val days:List<Int> = listOf(1)):TypeNotification()
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