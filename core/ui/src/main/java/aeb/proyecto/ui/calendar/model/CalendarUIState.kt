package aeb.proyecto.ui.calendar.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

/**
 * A generic, state-driven model representing the visual and data matrix of a calendar grid layout.
 * Decouples chronological presentation and selection tracking variables from concrete business logic entities
 * by leveraging a polymorphic polymorphic payload wrapper [T].
 *
 * @param T The specialized data token profile associated with individual calendar day variables.
 * @property dates The linear sequence list containing compiled structural matrix items.
 */
data class CalendarUIState<T>(
    val dates: List<DateCalendar<T>>
){

    /**
     * Internal atomic data token representing a singular day cell inside the calendar grid ecosystem.
     *
     * @property dateOfMonth The precise underlying platform [LocalDate] reference stamp.
     * @property isSelected Operational UI flag indicating whether the day cell is actively targeted by the user.
     * @property data Contextual customizable business model payload injected dynamically from downstream layers.
     */
    data class DateCalendar<T>(
        val dateOfMonth: LocalDate,
        val isSelected: Boolean,
        val data:T? = null
    )

    companion object {

        /**
         * Generates a neutralized, unpopulated boilerplate instance of the state container.
         */
        fun <T> init(): CalendarUIState<T> {
            return CalendarUIState(
                dates = emptyList()
            )
        }
    }
}

/**
 * Mathematical sequence extractor extension engineered to compile a flat grid of dates representing a targeted month view.
 * Guarantees a symmetrical layout matrix bounding precisely 42 days (6 weeks) to align leading offsets
 * from the trailing boundary of the prior month and leading fragments of the subsequent one cleanly.
 *
 * @param firstDayWeek The targeted platform day profile (e.g., [DayOfWeek.MONDAY]) designated to anchor week start rows.
 * @return A compiled sequence list of [LocalDate] entities representing the balanced grid matrix framework.
 */
fun YearMonth.getCalendarDates(firstDayWeek: DayOfWeek): List<LocalDate> {
    val firstOfMonth = atDay(1)

    // Compute the absolute mathematical start-of-grid anchor date based on the chosen week start alignment
    val start = firstOfMonth.with(TemporalAdjusters.previousOrSame(firstDayWeek))

    // Construct a linear incremental sequence to supply layout slots for a rigid 6x7 grid profile
    return generateSequence(start) { it.plusDays(1) }
        .take(42) // Strict 6 weeks * 7 days boundary guard to prevent graphic UI snapping artifacts
        .toList()
}
