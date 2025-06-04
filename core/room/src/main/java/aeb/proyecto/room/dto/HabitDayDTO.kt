package aeb.proyecto.room.dto

import aeb.proyecto.room.converters.BigDecimalConverter
import aeb.proyecto.room.entities.HabitDay
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class HabitDayDTO(
    var goalDone: BigDecimal = BigDecimal(0),
    val date: String = "",
    var hourFinishDate: String = ""
)

fun HabitDay.convertToDTO(): HabitDayDTO {
    return HabitDayDTO(
        goalDone = goalDone,
        date = date.toString(),
        hourFinishDate = hourFinishDate.toString()
    )
}

fun HabitDayDTO.convertToHabitDay(): HabitDay {
    return HabitDay(
        goalDone = goalDone,
        date = LocalDate.parse(date),
        hourFinishDate = LocalTime.parse(hourFinishDate)
    )
}
