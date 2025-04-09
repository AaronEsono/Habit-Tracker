package aeb.proyecto.room.entities.dayHabit

import aeb.proyecto.room.converters.BigDecimalConverter
import aeb.proyecto.room.entities.habit.Habit
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalTime
import java.math.BigDecimal

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["idHabit"],
            onDelete = ForeignKey.CASCADE, //elimina dailyHabits si se elimina el habit
            onUpdate = ForeignKey.CASCADE // actualiza dailyHabits si se actualiza el habit
        )
    ]
)
data class HabitDay(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    var idHabit:Long = 0,
    @TypeConverters(BigDecimalConverter::class)
    var goalDone:BigDecimal = BigDecimal(0),
    val date:LocalDate = LocalDate.now(),
    var hourFinishDate:LocalTime = LocalTime.now()
)