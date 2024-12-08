package aeb.proyecto.habittracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyHabit(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val idHabit:Int = 0,
    val timesDone:Int = 0,
    val date:String = "",
)