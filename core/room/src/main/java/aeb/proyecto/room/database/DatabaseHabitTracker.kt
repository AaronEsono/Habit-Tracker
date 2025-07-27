package aeb.proyecto.room.database

import aeb.proyecto.room.converters.BigDecimalConverter
import aeb.proyecto.room.converters.DateConverter
import aeb.proyecto.room.converters.IconConverter
import aeb.proyecto.room.converters.TimeConverter
import aeb.proyecto.room.converters.TypeHabitConverter
import aeb.proyecto.room.converters.TypeNotificationConverter
import aeb.proyecto.room.converters.UnitHabitConverter
import aeb.proyecto.room.dao.EntireHabitDao
import aeb.proyecto.room.dao.HabitWithDailyHabitDao
import aeb.proyecto.room.dao.HabitWithNotificationDao
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitNotification
import aeb.proyecto.room.entities.TimeEntry
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Habit::class, HabitDay::class,HabitNotification::class,TimeEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class,
    TimeConverter::class,
    IconConverter::class,
    BigDecimalConverter::class,
    TypeHabitConverter::class,
    UnitHabitConverter::class,
    TypeNotificationConverter::class)
abstract class DatabaseHabitTracker : RoomDatabase() {
    abstract fun habitWithNotificationDao(): HabitWithNotificationDao
    abstract fun entireDaoHabit(): EntireHabitDao
    abstract fun habitWithDailyHabitDao(): HabitWithDailyHabitDao
}