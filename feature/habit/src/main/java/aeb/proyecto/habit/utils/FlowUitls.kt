package aeb.proyecto.habit.utils

import aeb.proyecto.domain.usecase.habit.HabitDatastoreUseCase
import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.habit.model.pager.PagerSelected
import android.util.Log
import kotlinx.coroutines.flow.StateFlow

/**
 * Evaluates and establishes the default baseline category selection for the horizontal layout pager.
 * Executes a defensive cascading resolution fallback on a suspendable pipeline: synchronizes active
 * memory states, polls persistent low-latency user preferences from DataStore, or falls back straight
 * to the head node of the collections matrix if historical records are missing.
 *
 * @param sortedTypes Resolved and prioritized list of available category layout elements.
 * @param selectedType Upstream read-only state flow monitoring the current runtime pager state selection.
 * @param habitDatastoreUseCase Subsystem utility managing disk-bound configuration preference buffers.
 * @param updateSelected Callback lambda dispatcher to commit the resolved coordinate safely into mutable states.
 * @return True if a valid item was successfully attached and synchronized, false if any infrastructure anomaly occurs.
 */
suspend fun initializeSelectedTypeIfNeeded(
    sortedTypes: List<PagerElement>,
    selectedType: StateFlow<CurrentPagerSelection>,
    habitDatastoreUseCase: HabitDatastoreUseCase,
    updateSelected: (CurrentPagerSelection) -> Unit,
): Boolean {
    return try {
        val current = selectedType.value

        // CASCADE LAYER 1: Verify if an accurate programmatic selection is already residing in memory
        val selectedElement = when (current) {
            is CurrentPagerSelection.Selected -> {
                sortedTypes.find { it.tag == current.pagerSelected.pagerElement.tag }
            }
            else -> null
        }

        // CASCADE LAYER 2 & 3: Resolve missing states via persistent storage or fall back to the head node index
        val finalElement = selectedElement
            ?: run {
                val savedTag = habitDatastoreUseCase.getTypeSelected()
                sortedTypes.find { it.tag == savedTag }
            }
            ?: sortedTypes.firstOrNull()

        // Commit and broadcast the synchronized coordinates if a valid target is successfully mapped
        finalElement?.let {
            habitDatastoreUseCase.setSelectedHabitType(it.tag)
            updateSelected(
                CurrentPagerSelection.Selected(
                    PagerSelected(
                        pagerElement = it,
                        index = sortedTypes.indexOf(it)
                    )
                )
            )
        }

        true
    } catch (e: Exception) {
        false // Defensive barrier: catch infrastructure exceptions without disrupting the host flow lifecycle
    }
}