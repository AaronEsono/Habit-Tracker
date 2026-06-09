package aeb.proyecto.habit.components.common.bottomSheet.editHabit.state

import aeb.proyecto.room.entities.Habit


/**
 * Represents the distinct interaction states for the Edit Habit module.
 * This sealed hierarchy ensures type-safe state handling within the ViewModel
 * and reactive UI composition.
 */
sealed class EditHabitState{

    /** * Represents a failure state in data acquisition or persistence operations.
     * @param error A localized message or technical string detailing the failure.
     */
    data class Error (val error: String): EditHabitState()

    /** Indicates the module is actively fetching or initializing habit data. */
    data object Loading: EditHabitState()

    /** * Indicates a successful data retrieval or state sync.
     * @param habit The fully hydrated [Habit] entity ready for UI binding.
     */
    data class Success (val habit: Habit): EditHabitState()
}