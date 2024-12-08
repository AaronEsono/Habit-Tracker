package aeb.proyecto.habittracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val habitId:Int = 0,
    var hour:Int = 0,
    var minute:Int = 0,
)