package aeb.proyecto.room.entities.habit

import aeb.proyecto.room.converters.IconConverter
import aeb.proyecto.room.converters.TypeHabitConverter
import aeb.proyecto.room.converters.UnitHabitConverter
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.room.model.classes.UnitHabit
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fax
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    var name:String = "",
    var description:String? = "",

    val color: Int = 0,
    @TypeConverters(IconConverter::class)
    var icon:ImageVector = Icons.Filled.Fax,

    var goal:Float = 0f,

    @TypeConverters(UnitHabitConverter::class)
    var unit:UnitHabit = UnitHabit.TIMES,

    @TypeConverters(TypeHabitConverter::class)
    val typeHabit: TypeHabit = TypeHabit.Daily
)