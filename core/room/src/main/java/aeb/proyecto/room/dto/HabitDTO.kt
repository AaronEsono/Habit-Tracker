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

/**
 * Data transfer representation capturing the structural configuration and identity metadata of a habit.
 *
 * This DTO serializes complex architectural components (such as vector visual configurations, metrics
 * enums, and behavioral cadence sealed class structures) into localized, flat [String] values optimized
 * for network payloads or external document-based storage systems.
 *
 * @property name Descriptive structural label or title given to the tracking routine.
 * @property description Optional context or motivational footnotes anchoring the behavior.
 * @property color Hexadecimal integer color token targeting dynamic UI theme matching.
 * @property icon Flat serialized string representation of the associated vector asset identifier.
 * @property goal Quantitative measurement target configured for execution intervals.
 * @property unitHabit Flat serialized string discriminator mapping to the explicit [UnitHabit] metric classification.
 * @property typeHabit Flat serialized string discriminator mapping to the structural [TypeHabit] recurrence configuration.
 */
data class HabitDTO(
    var name:String = "",
    var description:String? = "",
    val color: Int = 0,
    var icon: String = "",
    var goal: BigDecimal = BigDecimal(0),
    var unitHabit: String = "",
    val typeHabit: String = ""
)

/**
 * Transforms a local persistent [Habit] database entity into a transport-ready [HabitDTO].
 *
 * This mapper utilizes dedicated domain type converters to dynamically serialize structural framework
 * objects (icons, units, and recurrence models) into scalar string values.
 *
 * @return A fully serialized [HabitDTO] instance.
 */
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

/**
 * Reconstructs a type-safe persistent [Habit] entity from a flat transport [HabitDTO] snapshot.
 *
 * This operation reverses the serialization pipeline, parsing raw structural strings back into
 * compile-time validated ecosystem metrics, vector layouts, and sealed pattern cadence rules.
 *
 * @return A fully populated [Habit] entity ready for database caching and local querying.
 */
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