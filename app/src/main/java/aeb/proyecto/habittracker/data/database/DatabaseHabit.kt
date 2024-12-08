package aeb.proyecto.habittracker.data.database

import aeb.proyecto.habittracker.data.dao.DailyHabitDao
import aeb.proyecto.habittracker.data.dao.HabitDao
import aeb.proyecto.habittracker.data.dao.HabitWithNofiticationDao
import aeb.proyecto.habittracker.data.dao.NotificationDao
import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.Notification
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Habit::class, Notification::class, DailyHabit::class],
    version = 1,
    exportSchema = true
)
abstract class DatabaseHabit : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun notificationDao(): NotificationDao
    abstract fun dailyHabitDao(): DailyHabitDao
    abstract fun habitWithNotificationDao(): HabitWithNofiticationDao
}