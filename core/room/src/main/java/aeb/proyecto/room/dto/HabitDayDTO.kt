package aeb.proyecto.room.dto

import aeb.proyecto.room.converters.BigDecimalConverter
import aeb.proyecto.room.entities.HabitDay
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

/**
 * Data transfer representation of a specific daily habit progress execution log.
 *
 * This DTO flattens calendar and temporal structures into clean, standardized [String] fields,
 * optimizing serialization layouts for cloud transport networks or cache serialization pipelines.
 *
 * @property goalDone The precise decimal amount accomplished or completed toward the target metric.
 * @property date Standard ISO-8601 calendar string representation (e.g., "YYYY-MM-DD") capturing the transaction date.
 * @property hourFinishDate Standard ISO-8601 clock string representation (e.g., "HH:mm:ss") capturing the progress timestamp.
 */
data class HabitDayDTO(
    var goalDone: BigDecimal = BigDecimal(0),
    val date: String = "",
    var hourFinishDate: String = ""
)

/**
 * Transforms a local persistent [HabitDay] database entity into a serialization-safe [HabitDayDTO].
 *
 * It automatically marshals temporal structures into their respective ISO-8601 string definitions.
 *
 * @return A transport-ready [HabitDayDTO] instance.
 */
fun HabitDay.convertToDTO(): HabitDayDTO {
    return HabitDayDTO(
        goalDone = goalDone,
        date = date.toString(),
        hourFinishDate = hourFinishDate.toString()
    )
}

/**
 * Reconstructs a persistent [HabitDay] entity from a transport [HabitDayDTO] snapshot.
 *
 * This mapper safely parses standardized ISO-8601 string representations back into type-safe
 * platform calendar objects ([LocalDate] and [LocalTime]).
 *
 * @return A fully populated [HabitDay] entity ready for Room persistence transactions.
 */
fun HabitDayDTO.convertToHabitDay(): HabitDay {
    return HabitDay(
        goalDone = goalDone,
        date = LocalDate.parse(date),
        hourFinishDate = LocalTime.parse(hourFinishDate)
    )
}
