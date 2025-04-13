package aeb.proyecto.habit.utils

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.habit.SelectedTypeState
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.habit.model.PagerSelected
import kotlinx.coroutines.flow.StateFlow
import java.time.DayOfWeek
import java.time.LocalDate

suspend fun initializeSelectedTypeIfNeeded(
    sortedTypes: List<PagerElement>,
    selectedType: StateFlow<SelectedTypeState>,
    datastore: DatastoreInterface,
    updateSelected: (SelectedTypeState) -> Unit,
): Boolean {
    return try {
        if (selectedType.value is SelectedTypeState.Uninitialized) {
            val savedTag = datastore.getTypeSelected() ?: "Daily"
            var pageElement = sortedTypes.find { it.tag == savedTag }

            if (pageElement == null && sortedTypes.isNotEmpty()) {
                pageElement = sortedTypes.first()
                datastore.setTypeSelectedDate(pageElement.tag)
            }

            pageElement?.let {
                updateSelected(
                    SelectedTypeState.Selected(
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