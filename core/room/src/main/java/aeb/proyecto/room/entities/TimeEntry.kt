package aeb.proyecto.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["idHabit"],
        )
    ]
)
data class TimeEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val typeTimer: Int = 0,
    val time: Long = 0L,
    val idHabit: Long? = 0L,
    val favourite: Boolean = false
)