package aeb.proyecto.timer.components.commom.bottomSheet.pickHabit

import aeb.proyecto.domain.usecase.timer.GetHabitUseCase
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.timer.model.HabitLinkedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PickHabitViewModel @Inject constructor(
    private val getHabitUseCase: GetHabitUseCase
):ViewModel() {

    val pickHabitUIState: StateFlow<PickHabitUIState> = getHabitUseCase.getAllHabitsWithTimeUnit()
        .map<List<Habit>, PickHabitUIState> { habits ->
            PickHabitUIState.Success(habits)
        }
        .catch {
            emit(PickHabitUIState.Error)
        }
        .onStart {
            emit(PickHabitUIState.Loading)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PickHabitUIState.Loading)


    private val _habitSelected: MutableStateFlow<Habit?> = MutableStateFlow(null)
    val habitSelected: StateFlow<Habit?> = _habitSelected.asStateFlow()

    private val _dateSelected: MutableStateFlow<LocalDate?> = MutableStateFlow(null)
    val dateSelected: StateFlow<LocalDate?> = _dateSelected.asStateFlow()

    private val _dialogOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val dialogOpen: StateFlow<Boolean> = _dialogOpen.asStateFlow()

    fun setData(habit:HabitLinkedState){
        when(habit){
            is HabitLinkedState.Data -> {
                _habitSelected.value = habit.data.habit
                _dateSelected.value = habit.data.day.date
            }
            HabitLinkedState.NoData -> {
                _habitSelected.value = null
                _dateSelected.value = null
            }
        }
    }

    fun habitSelected(habit: Habit){
        _habitSelected.update { habit }
    }

    fun openDialog(){
        _dialogOpen.update { true }
    }

    fun closeDialog(){
        _dialogOpen.update { false }
    }

    fun choseDate(date:LocalDate){
        _dateSelected.update { date }
    }

}

sealed class PickHabitUIState(){
    data class Success(val habits:List<Habit>):PickHabitUIState()
    object Loading:PickHabitUIState()
    object Error:PickHabitUIState()
}