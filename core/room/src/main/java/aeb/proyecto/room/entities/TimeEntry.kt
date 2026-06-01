package aeb.proyecto.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime


/**
 * Represents a dedicated time-tracking or stopwatch log entry within the persistent local storage.
 *
 * This entity supports both standalone stopwatch sessions and habit-bound duration metrics, tracking
 * execution lengths, rest intervals, structural configurations, and quick-access flags.
 *
 * @property id Unique auto-generated tracking key identifier.
 * @property typeTimer Core operational configuration mode identifying the structural stopwatch/timer archetype used.
 * @property time Total active operational duration logged during the focus session (expressed in milliseconds).
 * @property restTime Total auxiliary rest, downtime, or break duration logged during the interval (expressed in milliseconds).
 * @property intervals Total count of repeating focus/rest dynamic sequences completed throughout the tracking session.
 * @property idHabit Optional relational foreign key linking this specific time metric back to a parent [Habit] identity token.
 * @property lastTimeUsed The exact system calendar [LocalDateTime] marker capturing when this specific session layout was updated.
 * @property favourite Explicit user toggle marking this temporal layout configuration as a prioritized quick-access shortcut.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["idHabit"],
            onDelete = ForeignKey.CASCADE, // Clean up time logs automatically if the associated parent habit is deleted
            onUpdate = ForeignKey.CASCADE // Sync relational integrity state if primary keys shift
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