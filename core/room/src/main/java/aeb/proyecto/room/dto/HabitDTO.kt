package aeb.proyecto.room.dto

import aeb.proyecto.room.converters.BigDecimalConverter
import aeb.proyecto.room.converters.IconConverter
import aeb.proyecto.room.converters.TypeHabitConverter
import aeb.proyecto.room.converters.UnitHabitConverter
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.room.model.classes.UnitHabit
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fax
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.math.BigDecimal

data class HabitDTO(
    var name:String = "",
    var description:String? = "",
    val color: Int = 0,
    var icon: String = "",
    var goal: BigDecimal = BigDecimal(0),
    var unitHabit: String = "",
    val typeHabit: String = ""
)

fun Habit.convertToDTO():HabitDTO{
    return HabitDTO(
        name = name,
        description = description,
        color = color,
        icon = IconConverter().fromImageVector(icon),
        goal = goal,
        unitHabit = UnitHabitConverter().fromUnitHabit(unit),
        typeHabit = TypeHabitConverter().fromTypeHabit(typeHabit)
    )
}

fun HabitDTO.convertToHabit():Habit{
    return Habit(
        name = name,
        description = description,
        color = color,
        icon = IconConverter().toImageVector(icon),
        goal = goal,
        unit = UnitHabitConverter().toUnitHabit(unitHabit),
        typeHabit = TypeHabitConverter().toTypeHabit(typeHabit)
    )
}