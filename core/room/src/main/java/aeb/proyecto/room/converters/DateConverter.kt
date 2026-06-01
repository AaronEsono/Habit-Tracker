package aeb.proyecto.room.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Platform calendar persistence bridge converting modern temporal objects into epoch millisecond primitives.
 *
 * This converter standardizes [LocalDate] mappings using the active device's operational [ZoneId],
 * maintaining seamless time-zone alignment during date-shifting persistence operations.
 */
class DateConverter {

    /**
     * Reconstructs a type-safe [LocalDate] from a standard Unix epoch timestamp in milliseconds.
     *
     * @param value The raw epoch millisecond count extracted from the database cache.
     * @return The locally aligned [LocalDate] calendar reference, or null if the input data was missing.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
    }

    /**
     * Flattens a rich [LocalDate] reference into a primitive scalar millisecond representation tracking to the epoch.
     *
     * @param date The localized calendar date target vector.
     * @return The calculated [Long] millisecond timestamp mapped to the start of that calendar day, or null if unallocated.
     */
    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    }
}