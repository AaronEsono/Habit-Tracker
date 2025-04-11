package aeb.proyecto.habit

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.habit.model.PagerSelected
import aeb.proyecto.habit.model.findPagerElement
import aeb.proyecto.habit.model.orderPagerElements
import aeb.proyecto.habit.utils.getDateRangeByTag
import aeb.proyecto.habit.utils.initializeSelectedTypeIfNeeded
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo,
    private val datastoreInterface: DatastoreInterface
):ViewModel() {

    private val _dateSelected = MutableStateFlow(LocalDate.now())
    val dateSelected:StateFlow<LocalDate> = _dateSelected.asStateFlow()

    private val _selectedType = MutableStateFlow<SelectedTypeState>(SelectedTypeState.Uninitialized)
    val selectedType: StateFlow<SelectedTypeState> = _selectedType.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val typeUIState: StateFlow<TypeUIState> = habitWithDailyHabitRepo.getExistingTypesHabit()
        .map { types ->
            types.map { findPagerElement(it) }
                .sortedBy { orderPagerElements.indexOf(it) }
        }
        .flatMapLatest { sortedTypes ->
            flow {
                val initialized = initializeSelectedTypeIfNeeded(
                    sortedTypes = sortedTypes,
                    selectedType = selectedType,
                    updateSelected = { _selectedType.value = it },
                    datastore = datastoreInterface,
                )

                emit(
                    if (initialized) TypeUIState.Success(sortedTypes)
                    else TypeUIState.Error
                )
            }
        }
        .catch {
            emit(TypeUIState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TypeUIState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val habitUIState: StateFlow<HabitsUIState> = combine(
        typeUIState.filterIsInstance<TypeUIState.Success>(),
        selectedType,
        dateSelected
    ) { typeState, selectedIndex, selectedDate ->
        Triple(typeState, selectedIndex, selectedDate)
    }
        .flatMapLatest { (_, pagerSelected, selectedDate) ->
            flow {
                if (pagerSelected is SelectedTypeState.Uninitialized) {
                    emit(HabitsUIState.Empty)
                    return@flow
                }

                val tag = (pagerSelected as? SelectedTypeState.Selected)?.pagerSelected?.pagerElement?.tag ?: ""
                val (from, to) = getDateRangeByTag(tag, selectedDate)

                habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateAndType(from, to, tag)
                    .collect { habits -> emit(HabitsUIState.Success(habits))}

            }.catch {
                emit(HabitsUIState.Error)
            }
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HabitsUIState.Loading
    )

    fun onClickTab(pagerElement: PagerElement) = viewModelScope.launch{
        val types = (typeUIState.value as? TypeUIState.Success)?.availableTypes ?: return@launch
        val index = types.indexOf(pagerElement)

        _selectedType.value = SelectedTypeState.Selected(
            PagerSelected(index,pagerElement)
        )

        datastoreInterface.setTypeSelectedDate(pagerElement.tag)
    }

}

sealed class TypeUIState(){
    data class Success(val availableTypes:List<PagerElement>):TypeUIState()
    data object Error:TypeUIState()
    data object Loading:TypeUIState()
}

sealed class HabitsUIState(){
    data class Success(val habits:List<HabitWithDailyHabit>):HabitsUIState()
    data object Error:HabitsUIState()
    data object Loading:HabitsUIState()
    data object Empty:HabitsUIState()
}

sealed class SelectedTypeState {
    data object Uninitialized : SelectedTypeState()
    data class Selected(val pagerSelected: PagerSelected) : SelectedTypeState()
}