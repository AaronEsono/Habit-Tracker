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
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.HabitNotification
import aeb.proyecto.room.entities.TimeEntry
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Centralized relational persistence highway architecture for the local ecosystem.
 *
 * This monolithic abstract controller registers structural data models, marshals custom object
 * graph transformations via type converters, and exposes atomic data access objects (DAOs).
 */
@Database(
    entities = [Habit::class, HabitDay::class,HabitNotification::class,TimeEntry::class],
    version = 1,
    exportSchema = true,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2),
//        In case to delete or rename:
//        AutoMigration(from = 2, to = 3, spec = DatabaseHabitTracker.MigrationToXToY::class),
//    ]
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

    //@DeleteTable(tableName = "TableName")
    //@DeleteColumn(tableName = "TableName", columnName = "ColumnName")
    //@RenameTable(toTableName = "TableName", fromTableName = "NewTableName")
    //@RenameColumn(tableName = "TableName", fromColumnName = "ColumnName", toColumnName = "NewColumnName")
    //class MigrationToXToY: AutoMigrationSpec
}


// 1. Indicar en el gradle el path del schema
// 2. Indicar la migracion, de qué version a qué version
// 3. Cambia la version, el numero
// 4. Si hay que renombrar o borrar, crear una migrationSpec
// 5. Disfruta