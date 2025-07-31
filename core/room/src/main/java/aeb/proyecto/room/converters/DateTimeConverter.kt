package aeb.proyecto.room.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime {
        return Instant.ofEpochMilli(value)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    @TypeConverter
    fun dateToTimestamp(dateTime: LocalDateTime): Long {
        return dateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}