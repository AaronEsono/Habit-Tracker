package aeb.proyecto.room.model.habitCompressed

data class EntireHabitCompressed(
    val habit: HabitCompressed,
    val dailyHabits: List<DailyHabitCompressed>,
    val notifications: List<NotificationCompressed>
)