package aeb.proyecto.habit.utils

import aeb.proyecto.domain.usecase.habit.HabitDatastoreUseCase
import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.habit.model.pager.PagerSelected
import android.util.Log
import kotlinx.coroutines.flow.StateFlow

/**
 * Funcion que inicializa el tipo de hábito seleccionado por el usuario
 * pilla del datastore el ultimo valor, sino, pilla el primero de la lista
 */
suspend fun initializeSelectedTypeIfNeeded(
    sortedTypes: List<PagerElement>,
    selectedType: StateFlow<CurrentPagerSelection>,
    habitDatastoreUseCase: HabitDatastoreUseCase,
    updateSelected: (CurrentPagerSelection) -> Unit,
): Boolean {
    return try {
        val current = selectedType.value

        val selectedElement = when (current) {
            is CurrentPagerSelection.Selected -> {
                sortedTypes.find { it.tag == current.pagerSelected.pagerElement.tag }
            }
            else -> null
        }

        val finalElement = selectedElement
            ?: run {
                val savedTag = habitDatastoreUseCase.getTypeSelected()
                sortedTypes.find { it.tag == savedTag }
            }
            ?: sortedTypes.firstOrNull()

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
        false
    }
}