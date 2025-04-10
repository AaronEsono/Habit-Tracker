package aeb.proyecto.habit

import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.habit.model.findPagerElement
import aeb.proyecto.habit.model.orderPagerElements
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
):ViewModel() {

    private val _dateSelected = MutableStateFlow(LocalDate.now())
    val dateSelected:StateFlow<LocalDate> = _dateSelected.asStateFlow()

    private val _uiState = MutableStateFlow<HabitUIState>(HabitUIState.Loading)
    val uiState:StateFlow<HabitUIState> = _uiState.asStateFlow()

    val availableTypes:StateFlow<List<PagerElement>> = habitWithDailyHabitRepo.getExistingTypesHabit()
        .map { types ->
            types.map { type -> findPagerElement(type) }
                .sortedBy { type -> orderPagerElements.indexOf(type) }
        }
        .onStart {
            _uiState.value = HabitUIState.Loading
        }
        .onEach {
            _uiState.value = HabitUIState.Success
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

}

sealed class HabitUIState(){
    data object Success:HabitUIState()
    data object Error:HabitUIState()
    data object Loading:HabitUIState()
}