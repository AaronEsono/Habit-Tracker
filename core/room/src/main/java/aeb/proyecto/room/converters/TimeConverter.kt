package aeb.proyecto.room.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Spatial clock persistence bridge converting modern hour-minute timelines into primitive
 * epoch millisecond scalar values.
 *
 * This converter anchors pure [LocalTime] structures to the baseline Epoch Day Zero (1970-01-01)
 * using the active device's operational [ZoneId], standardizing persistent database storage.
 */
class TimeConverter {

    /**
     * Reconstructs a type-safe [LocalTime] clock configuration from a standard Unix epoch millisecond timestamp.
     *
     * Extracts exclusively the temporal clock matrix, discarding the underlying baseline epoch date layout.
     *
     * @param value The raw epoch millisecond count extracted from the database cache.
     * @return The locally aligned [LocalTime] reference, or null if the field was unallocated.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalTime? {
        return value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
        }
    }

    /**
     * Flattens a rich [LocalTime] coordinate into a primitive scalar millisecond tracking primitive.
     *
     * Internally chains the clock configuration to an arbitrary Epoch Day Zero calendar vector
     * to safely generate an absolute Unix timeline point.
     *
     * @param time The contextual local clock target vector tracking hours and minutes.
     * @return The calculated [Long] millisecond timestamp, or null if the input reference was missing.
     */
    @TypeConverter
    fun localTimeToTimestamp(time: LocalTime?): Long? {
        return time?.atDate(LocalDate.ofEpochDay(0))
            ?.atZone(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    }
}