package aeb.proyecto.room.model.classes

import java.time.DayOfWeek

/**
 * Sealed architectural hierarchy modeling the notification scheduling and dispatch behavior rules.
 *
 * This closed polymorphism tree encapsulates specialized configuration signatures for different
 * reminder strategies, ensuring type-safety during scheduling calculations.
 *
 * @property tag A unique, unified string indicator used primarily as a structural discriminator
 * token during database serialization pipelines.
 */
sealed class TypeNotification(val tag:String) {
    /**
     * Configuration targeting explicit calendar days within a standard week.
     *
     * @property days Collection of specific [DayOfWeek] markers when the notification is permitted to fire.
     */
    data class Daily(val days: List<DayOfWeek> = listOf(DayOfWeek.MONDAY)) : TypeNotification(DAILY_TAG)

    /**
     * Configuration targeting steady, incremental gaps between active alert dispatches.
     *
     * @property interval The quantitative frequency gap (e.g., every 'X' days) tracking execution steps.
     */
    data class Recurring(val interval: Int = 1) : TypeNotification(RECURRING_TAG)
}