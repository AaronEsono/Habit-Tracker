package aeb.proyecto.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

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
    val time: Long? = null,
    val restTime: Long? = null,
    val intervals: Long? = null,
    val idHabit: Long? = null,
    val lastTimeUsed: LocalDateTime = LocalDateTime.now(),
    val favourite: Boolean = false
)