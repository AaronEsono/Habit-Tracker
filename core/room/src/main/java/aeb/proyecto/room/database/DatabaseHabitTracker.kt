package aeb.proyecto.room.database

import aeb.proyecto.room.dao.DailyHabitDao
import aeb.proyecto.room.dao.EntireHabitDao
import aeb.proyecto.room.dao.HabitDao
import aeb.proyecto.room.dao.HabitWithNotificationDao
import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.Notification
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Habit::class, Notification::class, DailyHabit::class],
    version = 1,
    exportSchema = true
)
abstract class DatabaseHabitTracker : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun dailyHabitDao(): DailyHabitDao
    abstract fun habitWithNotificationDao(): HabitWithNotificationDao
    abstract fun entireDaoHabit(): EntireHabitDao
}