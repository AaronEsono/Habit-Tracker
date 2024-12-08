package aeb.proyecto.habittracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    @PrimaryKey
    val id:Int = 0,
    var name:String = "",
    var description:String? = "",
    var color:String = "",
    var icon:String = "",
    var times:Int = 0,
    var unit:String = "",
)