package aeb.proyecto.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE, //elimina notification si se elimina el habit
            onUpdate = ForeignKey.CASCADE // actualiza notification si se actualiza el habit
        )
    ]
)
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    var habitId:Long = 0,
    var hour:Int = 0,
    var minute:Int = 0
)