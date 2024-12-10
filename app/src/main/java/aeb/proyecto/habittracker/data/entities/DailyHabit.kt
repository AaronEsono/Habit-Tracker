package aeb.proyecto.habittracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyHabit(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    val idHabit:Long = 0,
    var timesDone:Int = 0,
    val date:String = "",
)