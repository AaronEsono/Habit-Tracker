package aeb.proyecto.timer.components.commom.bottomSheet.pickHabit

import aeb.proyecto.domain.usecase.timer.GetHabitUseCase
import aeb.proyecto.room.entities.Habit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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

}

sealed class PickHabitUIState(){
    data class Success(val habits:List<Habit>):PickHabitUIState()
    object Loading:PickHabitUIState()
    object Error:PickHabitUIState()
}