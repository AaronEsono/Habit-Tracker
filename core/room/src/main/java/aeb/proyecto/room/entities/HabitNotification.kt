package aeb.proyecto.room.entities

import aeb.proyecto.room.converters.TypeNotificationConverter
import aeb.proyecto.room.model.classes.TypeNotification
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalTime

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
data class HabitNotification(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    var habitId:Long = 0,
    val time:LocalTime = LocalTime.now(),
    @TypeConverters(TypeNotificationConverter::class)
    val type: TypeNotification = TypeNotification.Recurring(1)
)
