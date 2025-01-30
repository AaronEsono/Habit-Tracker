package aeb.proyecto.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

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
data class DailyHabit(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    var idHabit:Long = 0,
    var timesDone:Int = 0,
    val date:String = "",
    var hourFinishDate:String? = null
)