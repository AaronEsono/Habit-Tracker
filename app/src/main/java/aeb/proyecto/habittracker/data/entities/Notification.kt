package aeb.proyecto.habittracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    var habitId:Long = 0,
    var hour:Int = 0,
    var minute:Int = 0,
    var name:String = "",
    var timeInMillis:Long = 0L
)