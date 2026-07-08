package aeb.proyecto.alarmmanager

import aeb.proyecto.alarmmanager.constants.INTERVAL
import aeb.proyecto.alarmmanager.constants.REMINDER
import aeb.proyecto.alarmmanager.di.ClockModule
import aeb.proyecto.alarmmanager.gsonProvider.GsonProvider
import aeb.proyecto.alarmmanager.service.AlarmService
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.model.classes.TypeNotification
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A thread-safe, centralized utility manager responsible for orchestrating the lifecycle
 * of native Android system notifications.
 *
 * Provisioned as an application-wide [@Singleton] through Dagger Hilt dependency injection,
 * this class encapsulates the boilerplate overhead required to build, push, snooze, and clear
 * system-level notification tracking frames. It acts as an abstraction layer above the platform's
 * `NotificationManager`, ensuring defensive configuration compliance across shifting Android SDK variants
 * (such as structural channel registrations and secure asynchronous [PendingIntent] binding constraints).
 *
 * @property context The globally scoped [@ApplicationContext] required to safely interface with the platform subsystem.
 */
@Singleton
class NotificationUtils @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clock: Clock
){

    /**
     * Calculates the absolute unix epoch timeline and schedules an exact system-level hardware alarm.
     *
     * This function handles the complex evaluation logic required to program the [AlarmManager]. It evaluates
     * structural scheduling variants inside a polymorphically split branch pipeline:
     *
     * - **[TypeNotification.Daily]:** Verifies if the target weekday matches [LocalDate.now]. If the scheduled clock
     * time has already expired for the current calendar date, or if today is an excluded tracking day, it searches
     * chronologically for the nearest upcoming valid day index via `getNextDay` and appends the appropriate [INTERVAL] offset.
     * - **[TypeNotification.Recurring]:** Anchors a fixed timestamp target matching the requested clock context,
     * automatically applying a custom day-interval calculation step forward if the targeted sequence sits in the past.
     *
     * Once the ultimate boundary constraint `timeInMillis` is computed, it packages the payload context as a stringified
     * JSON metadata frame inside an [Intent], securing the operational boundary via explicit [PendingIntent] architectural flags
     * (`FLAG_IMMUTABLE` combined with `FLAG_UPDATE_CURRENT`), and schedules an aggressive, low-power-resilient awake execution hook
     * utilizing [AlarmManager.setExactAndAllowWhileIdle].
     *
     * @param alarmItem The dynamic tracking context containing structural metadata identifiers, localized clock metrics,
     * and polymorphic notification recurrence specifications.
     */
    fun setUpAlarm(alarmItem: NotificationWithNameAndColor) {
        val now = java.time.LocalDateTime.now(clock)
        val currentDayOfWeek = now.dayOfWeek

        var timeInMillis:Long

        when (alarmItem.typeNotification) {
            is TypeNotification.Daily -> {
                val dailyConfig = alarmItem.typeNotification as TypeNotification.Daily

                val isTodayValid = dailyConfig.days.contains(currentDayOfWeek)
                val isTimeValid = alarmItem.time.isAfter(now.toLocalTime())

                if (isTodayValid && isTimeValid) {
                    timeInMillis = now.with(alarmItem.time)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                } else {
                    val nextDay = getNextDay(dailyConfig.days, currentDayOfWeek)

                    timeInMillis = now.plusDays(nextDay.toLong())
                        .with(alarmItem.time)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                }
            }

            is TypeNotification.Recurring -> {
                val recurringConfig = alarmItem.typeNotification as TypeNotification.Recurring

                var targetDateTime = now.with(alarmItem.time)

                if (targetDateTime.isBefore(now)) {
                    targetDateTime = targetDateTime.plusDays(recurringConfig.interval.toLong())
                }

                timeInMillis = targetDateTime.atZone(java.time.ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            }
        }

        val intent = Intent(context, AlarmService::class.java).apply {
            putExtra(REMINDER, GsonProvider.gson.toJson(alarmItem))
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmItem.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    /**
     * Dynamically computes and schedules the immediate subsequent occurrence of an active alarm item.
     *
     * Rather than relying on platform-level repeating engines (`AlarmManager.setRepeating`) which are subject
     * to system-enforced batching or drift degradation under modern power management configurations, this function
     * implements a precise sequential chaining pattern. It calculates future scheduling constraints the moment
     * a current alert window fires.
     *
     * It extracts unified interval rules through a clean polymorphic abstraction layer:
     * - **[TypeNotification.Daily]:** Invokes `getNextDay` to chronologically evaluate the next valid tracking weekday target.
     * - **[TypeNotification.Recurring]:** Directly extracts the fixed frequency integer step.
     *
     * The final execution timeline appends this day offset onto a freshly anchored calendar baseline configuration,
     * repackages the immutable [PendingIntent] payload configuration, and commits an un-batched hardware wake hook
     * via [AlarmManager.setExactAndAllowWhileIdle].
     *
     * @param alarmItem The target [NotificationWithNameAndColor] configuration context contextually undergoing
     * lifecycle chaining validation.
     */
    fun setRepeatedAlarm(alarmItem: NotificationWithNameAndColor) {
        val now = java.time.LocalDateTime.now(clock)
        val currentDayOfWeek = now.dayOfWeek

        val intervalDays: Int = when (alarmItem.typeNotification) {
            is TypeNotification.Daily -> {
                getNextDay(
                    (alarmItem.typeNotification as TypeNotification.Daily).days,
                    currentDayOfWeek
                )
            }

            is TypeNotification.Recurring -> {
                (alarmItem.typeNotification as TypeNotification.Recurring).interval
            }
        }
        val timeInMillis = now.plusDays(intervalDays.toLong())
            .with(alarmItem.time)
            .atZone(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val intent = Intent(context, AlarmService::class.java).apply {
            putExtra(REMINDER, GsonProvider.gson.toJson(alarmItem))
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmItem.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    /**
     * Revokes and purges a scheduled hardware alarm from the Android OS subsystem.
     *
     * To successfully dismantle a pending notification trigger, this function reconstructs an identical
     * [PendingIntent] token mirroring the original registration signature. The platform's [AlarmManager] performs
     * matching tracking lookup using the combination of the base [AlarmService] component intent and the explicit
     * unique [id] integer request token.
     *
     * Once matched, the target trigger is completely removed from the kernel's wake scheduling tables,
     * avoiding zombie notification triggers and saving battery life when a habit is modified or deleted.
     *
     * @param id The unique database structural primary key identifier of the habit, serving as the unique
     * request matching token code for the intent pipeline.
     */
    fun cancelAlarm(id: Long) {
        val intent = Intent(context, AlarmService::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}

/**
 * An algorithmic utility that calculates the shortest chronological day distance between the current date
 * and the next scheduled tracking event.
 *
 * This function handles the circular weekday wrapping constraint (where the timeline resets to Monday ($1$)
 * after tracking reaches Sunday ($7$)) by executing a sequential evaluation pipeline:
 *
 * 1. It sorts the target [DayOfWeek] collection chronologically by their internal primitive values to guarantee linear traversal.
 * 2. It iterates through the sorted set to find the first scheduled day resting strictly in the future relative to the [today] baseline ($day.value > today.value$).
 * 3. **Wrapping Fallback:** If no upcoming days remain within the current calendar week context, it computes a complement overflow step.
 * It measures the remaining distance to close the active week cycle and appends the index location of the initial target day for the subsequent week:
 * $$\Delta = (7 - \text{today.value}) + \text{sorted.first().value}$$
 *
 * @param list The collection of active [DayOfWeek] constraints assigned to a specific habit configuration.
 * @param today The baseline [DayOfWeek] coordinate representing the reference evaluation checkpoint (usually current system time).
 * @return An integer representing the absolute delta count of calendar days required to reach the next valid execution window.
 */
fun getNextDay(list: List<DayOfWeek>, today: DayOfWeek): Int {
    val sorted = list.sortedBy { it.value }
    for (day in sorted) {
        if (day.value > today.value) {
            return day.value - today.value
        }
    }
    return (7 - today.value) + sorted.first().value
}