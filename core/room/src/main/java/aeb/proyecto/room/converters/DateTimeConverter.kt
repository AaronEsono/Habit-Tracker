package aeb.proyecto.room.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * System clock persistence bridge converting high-precision modern date-time stamps
 * into primitive epoch millisecond scalar values.
 *
 * This converter manages full date and time coordinates utilizing the active device's operational
 * [ZoneId] matrix to guarantee absolute timeline sync during persistent read/write loops.
 */
class DateTimeConverter {

    /**
     * Reconstructs a type-safe [LocalDateTime] instance from a standard Unix epoch millisecond timestamp.
     *
     * @param value The raw epoch millisecond count extracted from the local database layer.
     * @return The locally aligned [LocalDateTime] matrix reference, or null if the field was unallocated.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }

    /**
     * Flattens a rich [LocalDateTime] coordinate into a primitive scalar millisecond tracking primitive.
     *
     * @param dateTime The contextual local date-time target vector.
     * @return The calculated [Long] millisecond timestamp mapped exactly to that calendar timeline cross-section,
     * or null if the input reference was missing.
     */
    @TypeConverter
    fun dateToTimestamp(dateTime: LocalDateTime?): Long? {
        return dateTime
            ?.atZone(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    }
}