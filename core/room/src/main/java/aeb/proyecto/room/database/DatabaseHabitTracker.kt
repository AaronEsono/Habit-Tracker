package aeb.proyecto.room.database

import aeb.proyecto.room.converters.BigDecimalConverter
import aeb.proyecto.room.converters.DateConverter
import aeb.proyecto.room.converters.DateTimeConverter
import aeb.proyecto.room.converters.IconConverter
import aeb.proyecto.room.converters.TimeConverter
import aeb.proyecto.room.converters.TypeHabitConverter
import aeb.proyecto.room.converters.TypeNotificationConverter
import aeb.proyecto.room.converters.UnitHabitConverter
import aeb.proyecto.room.dao.EntireHabitDao
import aeb.proyecto.room.dao.HabitWithDailyHabitDao
import aeb.proyecto.room.dao.HabitWithNotificationDao
import aeb.proyecto.room.dao.TimerEntryDao
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitNotification
import aeb.proyecto.room.entities.JIJIJJA
import aeb.proyecto.room.entities.TimeEntry
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Habit::class, HabitDay::class,HabitNotification::class,TimeEntry::class, JIJIJJA::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(DateConverter::class,
    TimeConverter::class,
    IconConverter::class,
    BigDecimalConverter::class,
    TypeHabitConverter::class,
    UnitHabitConverter::class,
    DateTimeConverter::class,
    TypeNotificationConverter::class)
abstract class DatabaseHabitTracker : RoomDatabase() {
    abstract fun habitWithNotificationDao(): HabitWithNotificationDao
    abstract fun entireDaoHabit(): EntireHabitDao
    abstract fun habitWithDailyHabitDao(): HabitWithDailyHabitDao
    abstract fun timerEntryDao(): TimerEntryDao
}


// 1. Indicar en el gradle el path del schema
// 2. Indicar la migracion, de qué version a qué version, poner solo una autoMigration, si pones varias no funcionará porque se va a pensar que hay que hacerlo
// de nuevo
// 3. Cambia la version, el numero
// 4. Disfruto


// Mañana: comprobar como funcionan los borrados y las versiones, probar a borrar tablas y/o variables