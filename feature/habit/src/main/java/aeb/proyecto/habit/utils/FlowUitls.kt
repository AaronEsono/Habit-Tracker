package aeb.proyecto.habit.utils

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.habit.model.PagerSelected
import kotlinx.coroutines.flow.StateFlow

/*
    Funcion que inicializa el tipo de hábito seleccionado por el usuario

    pilla del datastore el ultimo valor, sino, pilla el primero de la lista
 */
suspend fun initializeSelectedTypeIfNeeded(
    sortedTypes: List<PagerElement>,
    selectedType: StateFlow<CurrentPagerSelection>,
    datastore: DatastoreInterface,
    updateSelected: (CurrentPagerSelection) -> Unit,
): Boolean {
    return try {
        if (selectedType.value is CurrentPagerSelection.Uninitialized) {
            val savedTag = datastore.getTypeSelected() ?: "Daily"
            var pageElement = sortedTypes.find { it.tag == savedTag }

            if (pageElement == null && sortedTypes.isNotEmpty()) {
                pageElement = sortedTypes.first()
                datastore.setTypeSelectedDate(pageElement.tag)
            }

            pageElement?.let {
                updateSelected(
                    CurrentPagerSelection.Selected(
                        PagerSelected(
                            pagerElement = it,
                            index = sortedTypes.indexOf(it)
                        )
                    )
                )
            }
        }
        true
    } catch (e: Exception) {
        false
    }
}