package aeb.proyecto.habittracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    var name:String = "",
    var description:String? = "",
    var color:Int = 0,
    var icon:String = "",
    var times:Int = 0,
    var unit:Int = 0,
)