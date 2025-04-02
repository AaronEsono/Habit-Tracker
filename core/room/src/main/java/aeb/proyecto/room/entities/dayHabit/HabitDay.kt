package aeb.proyecto.room.entities.dayHabit

import aeb.proyecto.room.entities.habit.Habit
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

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
    var goalDone:Int = 0,
    val date:LocalDate = LocalDate.now(),
    var hourFinishDate:LocalTime = LocalTime.now()
)