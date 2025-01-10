package aeb.proyecto.habittracker.data.model.firestoreHabit

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.Notification

data class CompleteHabitCompressed(
    val habit: HabitCompressed,
    val dailyHabits: List<DailyHabitCompressed>,
    val notifications: List<NotificationCompressed>
)

data class DailyHabitCompressed(
    var timesDone:Int = 0,
    val date:String = ""
)

data class NotificationCompressed(
    var hour:Int = 0,
    var minute:Int = 0
)

data class HabitCompressed(
    var name:String = "",
    var description:String? = "",
    var color:Int = 0,
    var icon:String = "",
    var times:Int = 0,
    var unit:Int = 0,
)