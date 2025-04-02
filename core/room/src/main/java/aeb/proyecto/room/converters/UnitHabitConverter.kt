package aeb.proyecto.room.converters

import aeb.proyecto.room.model.classes.UnitHabit
import androidx.room.TypeConverter

class UnitHabitConverter {
    @TypeConverter
    fun fromUnitHabit(unitHabit: UnitHabit): String {
        return unitHabit.name
    }

    @TypeConverter
    fun toUnitHabit(name: String): UnitHabit {
        return UnitHabit.valueOf(name)
    }
}