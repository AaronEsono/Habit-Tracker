package aeb.proyecto.habittracker.data.database

import aeb.proyecto.habittracker.data.dao.HabitDao
import aeb.proyecto.habittracker.data.entities.Habit
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1, exportSchema = true)
abstract class DatabaseHabit: RoomDatabase() {
    abstract fun habitDao(): HabitDao
}