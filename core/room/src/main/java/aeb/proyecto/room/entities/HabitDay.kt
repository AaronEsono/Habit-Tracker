package aeb.proyecto.room.entities

import aeb.proyecto.room.converters.BigDecimalConverter
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalTime
import java.math.BigDecimal

/**
 * Represents a specific daily execution snapshot or metric log for a parent habit tracking pipeline.
 *
 * This entity forms a localized relational boundary via a Foreign Key mapping back to the [Habit]
 * entity database record, holding transaction states for quantitative goals met on explicit dates.
 *
 * @property id Unique auto-generated tracking key identifier for this specific daily record snapshot.
 * @property idHabit The relational foreign key referencing the upstream parent [Habit] identity token.
 * @property goalDone The precise decimal amount accomplished or completed toward the goal during this session.
 * @property date The calendar [LocalDate] tracking boundary documenting when the transaction took place.
 * @property hourFinishDate The exact timestamp mark [LocalTime] indicating when the last completion progress was logged.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["idHabit"],
            onDelete = ForeignKey.CASCADE, // Clean up historical days automatically if parent is deleted
            onUpdate = ForeignKey.CASCADE // Sync relational integrity state if primary keys shift
        )
    ]
)
data class HabitDay(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    var idHabit:Long = 0,
    @TypeConverters(BigDecimalConverter::class)
    var goalDone:BigDecimal = BigDecimal(0),
    val date:LocalDate = LocalDate.now(),
    var hourFinishDate:LocalTime = LocalTime.now()
)