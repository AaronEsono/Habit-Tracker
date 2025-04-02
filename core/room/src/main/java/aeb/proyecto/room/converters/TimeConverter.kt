package aeb.proyecto.room.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class TimeConverter {
    @TypeConverter
    fun fromTimeStampToLocalTime(value: Long): LocalTime {
        return Instant.ofEpochMilli(value)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
    }

    @TypeConverter
    fun localTimeToTimestamp(time: LocalTime): Long {
        return time.atDate(LocalDate.ofEpochDay(0))
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}