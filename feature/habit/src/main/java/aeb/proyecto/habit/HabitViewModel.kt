package aeb.proyecto.habit

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.habit.model.findPagerElement
import aeb.proyecto.habit.model.orderPagerElements
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo,
    private val datastoreInterface: DatastoreInterface
):ViewModel() {

    private val _dateSelected = MutableStateFlow(LocalDate.now())
    val dateSelected:StateFlow<LocalDate> = _dateSelected.asStateFlow()

    private val _selectedType = MutableStateFlow<Int>(0)
    val selectedType: StateFlow<Int> = _selectedType.asStateFlow()

    val typeUIState: StateFlow<TypeUIState> = habitWithDailyHabitRepo.getExistingTypesHabit()
        .map<List<String>, TypeUIState> { types ->
            val sortedTypes = types
                .map { type -> findPagerElement(type) }
                .sortedBy { type -> orderPagerElements.indexOf(type) }
            TypeUIState.Success(sortedTypes)
        }
        .catch {
            emit(TypeUIState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TypeUIState.Loading
        )

    fun onClickTab(index:Int){
        _selectedType.update { index }
    }

}

sealed class TypeUIState(){
    data class Success(val availableTypes:List<PagerElement>):TypeUIState()
    data object Error:TypeUIState()
    data object Loading:TypeUIState()
}

sealed class HabitsUIState(){
    data object Success:HabitsUIState()
    data object Error:HabitsUIState()
    data object Loading:HabitsUIState()
}