package aeb.proyecto.room.entities

import aeb.proyecto.room.converters.BigDecimalConverter
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
import java.math.BigDecimal

/**
 * Core structural model representing a primary habit configuration registry within the local database.
 *
 * This entity defines the identity, metadata, visual representation, and completion goals
 * tracking configuration for any user-defined metric pipeline.
 *
 * @property id Unique auto-generated tracking key identifier.
 * @property name Descriptive structural label or title given to the tracking routine.
 * @property description Optional context or motivational footnotes anchoring the behavior.
 * @property color Hexadecimal integer color token targeting dynamic UI theme matching.
 * @property icon The visual vector graphic asset bound to the specific habit representation.
 * @property goal Quantitative measurement target configured for execution intervals (using precision decimals).
 * @property unit The explicit metric classification type bounding the configured [goal].
 * @property typeHabit The structural operational schedule frequency profile (e.g., Daily, Custom).
 */
@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    var name:String = "",
    var description:String? = "",
    val color: Int = 0,
    @TypeConverters(IconConverter::class)
    var icon:ImageVector = Icons.Filled.Fax,

    @TypeConverters(BigDecimalConverter::class)
    var goal:BigDecimal = BigDecimal(0),

    @TypeConverters(UnitHabitConverter::class)
    var unit:UnitHabit = UnitHabit.TIMES,

    @TypeConverters(TypeHabitConverter::class)
    val typeHabit: TypeHabit = TypeHabit.Daily
)